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
  // Initializing database managers and geographic coordinates
  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)
  private val coord = Coordinates(41.8781.asInstanceOf, -87.6298.asInstanceOf)

  // Function to extract temperature from the JSON response body
  def extractTemperature(body: String): ZIO[Any, Throwable, Double] =
    for {
      _ <- ZIO.debug(s"body is : $body") // Displaying the response body for debugging
      json <- ZIO.fromEither(parse(body)) // Parsing the JSON response
      main <- ZIO.fromEither(json.hcursor.downField("main").as[Json]) // Accessing the "main" part of the JSON
      temp <- ZIO.fromEither(main.hcursor.downField("temp").as[Double]) // Extracting the temperature
    } yield temp

  // Function to perform the request to the OpenWeatherMap API
  def fetchData(): ZIO[Client, Throwable, Response] = {
    val url = URL
      .decode(s"https://api.openweathermap.org/data/2.5/weather" +
        s"?lat=${coord.latitude}&lon=${coord.longitude}" +
        s"&appid=d1973fd69722dbf4497b5701fb60f939")
      .toOption
      .getOrElse(throw new RuntimeException("Invalid URL"))

    for {
      client <- ZIO.service[Client]
      res <- client.url(url).get("/") // Performing the GET request to the specified URL
    } yield res
  }

  // Main logic of the application
  val appLogic: ZIO[Client, Throwable, Unit] =
    ZIO.debug("Fetch data starting...") *> // Debug message
      ZStream(fetchData())
        .repeat(Schedule.spaced(5.minutes)) // Repeating the request every 5 minutes
        .mapZIO { z =>
          for {
            res <- z
            body <- res.body.asString // Retrieving the response body as a string
          } yield body
        }
        .foreach(body =>
          for {
            temp <- extractTemperature(body)
            _ <- ZIO.debug(s"Extracted temperature=$temp") // Displaying the extracted temperature
            _ = dataModelDao.insertTempInDB(temp, coord) // Inserting the temperature into the database
          } yield temp
        )
}
