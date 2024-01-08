import controller.{TemperatureController, WeatherController}
import zio.*
import zio.http.*

object MainApp extends ZIOAppDefault {
  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    val httpApps = TemperatureController() ++ WeatherController()
    Server
        .serve(
          httpApps.withDefaultErrorResponse
        )
        .provide(
          Server.defaultWithPort(8080),
        )
}
