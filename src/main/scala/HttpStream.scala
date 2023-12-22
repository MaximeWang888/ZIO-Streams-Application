import zio._
import zio.stream._
import zio.Duration._
import zio.http._

object HttpStream extends ZIOAppDefault {
  val baseUrl = URL
    .decode(
      "https://api.openweathermap.org/data/2.5/weather"
    )
    .toOption
    .get

  def fetchData(latitude: String = "50", longitude: String = "34") = {
    val url = baseUrl
      .addQueryParams(
        QueryParams(
          Map(
            "lat" -> Chunk(latitude),
            "lon" -> Chunk(longitude),
            "appid" -> Chunk(
              "d1973fd69722dbf4497b5701fb60f939"
            ) // put it in a configuration file
          )
        )
      )

    for {
      client <- ZIO.service[Client]
      res <- client
        .url(url)
        .get("/")
    } yield res
  }

  override def run: ZIO[Any, Any, Unit] = {
    val appLogic = for {
      _ <- ZStream(fetchData())
        .repeat(Schedule.spaced(10.seconds))
        // .groupedWithin(30, 10.seconds)
        .mapZIO { z =>
          for {
            res <- z
            body <- res.body.asString
            _ <- Console.printLine(s"body size is: ${body.length}")
          } yield body
        }
        .foreach(Console.printLine(_))
    } yield ()

    appLogic.provide(Client.default, Scope.default)
  }
}
