import controller.{TemperatureController, WeatherController}
import service.TemperatureStream.appLogic
import zio.*
import zio.http.*

object MainApp extends ZIOAppDefault {
  private val serverPort = 8080

  // ZIOAppDefault requires a 'run' method to start the application
  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    // Combine the controllers to create HTTP apps
    val httpApps = TemperatureController() ++ WeatherController()

    // Define the server ZIO
    val server: ZIO[Any, Throwable, Nothing] =
      ZIO.debug(s"The server is starting at http://localhost:$serverPort ...") *> // Debug message
        Server.serve(httpApps.withDefaultErrorResponse) // Start serving the HTTP apps
          .provide(Server.defaultWithPort(8080) ++ Client.default) // Configure server settings
          .logError("Error when starting server ... ") // Log any errors during server startup

    // Define the background process ZIO
    val backgroundProcess: ZIO[Any, Throwable, Any] =
      ZIO.debug("Background process running...") *> appLogic.provide(Client.default) // Run the background process

    // Run the server and background process concurrently, and return the result of the first one that succeeds
    server.raceFirst(backgroundProcess)
}
