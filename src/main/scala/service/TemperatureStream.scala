package service

import zio.*
import zio.stream.ZStream
import zio.Duration.*
import zio.http.{Client, Response, URL}
import io.circe.Json
import io.circe.parser.*
import model.DataModel.Coordinates
import model.{DataModelDao, DatabaseManager}

object TemperatureStream {
  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)
  private val coord = Coordinates(41.8781.asInstanceOf, -87.6298.asInstanceOf)

  def extractTemperature(body: String): ZIO[Any, Throwable, Double] =
    for {
      _ <- ZIO.debug(s"body is : $body")
      json <- ZIO.fromEither(parse(body))
      main <- ZIO.fromEither(json.hcursor.downField("main").as[Json])
      temp <- ZIO.fromEither(main.hcursor.downField("temp").as[Double])
    } yield temp

  def fetchData(): ZIO[Client, Throwable, Response] = {
    val url = URL
      .decode(s"https://api.openweathermap.org/data/2.5/weather" +
        s"?lat=${coord.latitude}&lon=${coord.longitude}" +
        s"&appid=d1973fd69722dbf4497b5701fb60f939")
      .toOption
      .getOrElse(throw new RuntimeException("Invalid URL"))
    for {
      client <- ZIO.service[Client]
      res <- client.url(url).get("/")
    } yield res
  }

  val appLogic: ZIO[Client, Throwable, Unit] =
    ZIO.debug("Fetch data starting...") *>
      ZStream(fetchData())
        .repeat(Schedule.spaced(5.minutes))
        .mapZIO { z =>
          for {
            res <- z
            body <- res.body.asString
          } yield body
        }
        .foreach(body =>
          for {
            temp <- extractTemperature(body)
            _ <- ZIO.debug(s"Extract temperature=$temp")
            _ = dataModelDao.insertTempInDB(temp, coord)
          } yield temp
        )
}

