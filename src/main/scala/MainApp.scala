import controller.{TemperatureController, WeatherController}
import zio.*
import zio.http.*

object MainApp extends ZIOAppDefault {
  private val serverPort = 8080

  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    val httpApps = TemperatureController() ++ WeatherController()
    println(s"The server is starting at http://localhost:$serverPort ...")
    Server
        .serve(

          httpApps.withDefaultErrorResponse
        )
        .provide(
          Server.defaultWithPort(8080),
        )
        .logError("Error when starting server ... ")
}
