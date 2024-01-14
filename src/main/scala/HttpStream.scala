import zio.ZIOAppDefault
import zio.ZIO
import zio.ZIOAppArgs
import zio.Scope
import zio.http.Client
import zio.http.Method
import zio.Console
import io.circe.parser._
import io.circe.Json

object HttpStream extends ZIOAppDefault {
  private final val BASE_URL = "https://api.openweathermap.org/data/2.5/weather"
  private final val API_KEY = "d1973fd69722dbf4497b5701fb60f939"

  def createRequest(lat: Double, lon: Double): ZIO[Client, Throwable, String] =
    val queryParams = s"lat=43&lon=43&appid=$API_KEY"
    Client
      .request(
        s"$BASE_URL?$queryParams",
        Method.GET
      )
      .flatMap(getRes => getRes.body.asString)

  def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    val program: ZIO[Client, Throwable, String] = createRequest(3, 4)

    program.provide(Client.default).flatMap { result =>
      var temperature = for {
        json <- parse(result).toOption
        main <- json.hcursor.downField("main").as[Json].toOption
        temp <- main.hcursor.downField("temp").as[Double].toOption
      } yield temp

      ZIO.succeed(result)
    }
}
