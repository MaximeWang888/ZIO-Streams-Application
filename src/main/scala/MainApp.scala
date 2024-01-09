import controller.{TemperatureController, WeatherController}
import model.{DataModelDao, DatabaseManager}
import zio.*
import zio.http.*

object MainApp extends ZIOAppDefault {
  private val lookingCity = "Los Angeles"
  private val serverPort = 8080

  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    val dbManager = new DatabaseManager()
    val dataModelDao = new DataModelDao(dbManager)

    try {
      val city = dataModelDao.getCityToCoord(lookingCity)
      city match {
        case Some(city) =>
          println(s"City: ${city.name}, Coordinates: ${city.coordinates}")
        case None =>
          println(s"No city found in DB matching with ${lookingCity}")
      }

      val cityRecommendationOption = dataModelDao.getCityRecommendation(lookingCity)
      cityRecommendationOption match {
        case Some(cityRecommendation) =>
          println(s"City: ${lookingCity} recommendation is ${cityRecommendation.temperatureRange}")
        case None =>
          println(s"No recommendation found for $lookingCity")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      dbManager.closeConnection()
    }

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
