import controller.{TemperatureController, WeatherController}
import service.TemperatureStream.appLogic
import zio.*
import zio.http.*

object MainApp extends ZIOAppDefault {
  private val serverPort = 8080

  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    val httpApps = TemperatureController() ++ WeatherController()

    val server: ZIO[Any, Throwable, Nothing] =
      ZIO.debug(s"The server is starting at http://localhost:$serverPort ...") *>
        Server.serve(httpApps.withDefaultErrorResponse)
              .provide(Server.defaultWithPort(8080) ++ Client.default)
              .logError("Error when starting server ... ")

    val backgroundProcess: ZIO[Any, Throwable, Any] =
      ZIO.debug("Background process running...") *> appLogic.provide(Client.default)

    server.raceFirst(backgroundProcess)
}
