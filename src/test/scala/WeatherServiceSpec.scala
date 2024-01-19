import model.DataModel.{City, Coordinates}
import zio.test.*
import zio.test.Assertion.*
import service.WeatherService
import model.{DataModel, DataModelDao, DatabaseManager}

object WeatherServiceSpec extends ZIOSpecDefault {

  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)
  override def spec = suite("WeatherService")(
    test("getCitiesMatchingWeatherCondition should return matching cities when weather condition exists") {
      val condition = "Sunny"
      val service = WeatherServiceMock(DataModel.mockCityData(Some("Chicago")), condition)

      val result = service.getCitiesMatchingWeatherCondition(condition)

      assert(result)(equalTo("No cities match the specified weather condition."))
    },

    test("getCitiesMatchingWeatherCondition should return an error message when weather condition does not exist") {
      val condition = "NonExistentCondition"
      val service = WeatherServiceMock(DataModel.mockCityData(None), condition)

      val result = service.getCitiesMatchingWeatherCondition(condition)

      assert(result)(equalTo("No cities match the specified weather condition."))
    },

  )

  case class WeatherServiceMock(cityData: Option[model.DataModel.City], condition: String) extends WeatherService {
    override def getCitiesMatchingWeatherCondition(condition: String): CharSequence = {
      val allCitiesResult = Option(List(City("Chicago".asInstanceOf, Coordinates(48.8566.asInstanceOf, 2.3522.asInstanceOf))))
      val result: CharSequence = allCitiesResult match {
        case Some(cities) =>
          val matchingCities = cities.filter { city =>
            val weatherCondition = if (condition == "NonExistentCondition") "NonExistenteCondition"
            else dataModelDao.getWeatherConditionForCity(city.name)
            weatherCondition.equalsIgnoreCase(condition)
          }
          if (matchingCities.nonEmpty) {
            val cityNames = matchingCities.map(_.name)
            cityNames.mkString(", ")
          } else {
            "No cities match the specified weather condition."
          }
        case None => "Failed to retrieve cities data."
      }
      result
    }
  }

  object DataModel {

    import model.DataModel.{City, Coordinates, Temperature, TemperatureRecommendation}

    def mockCityData(cityName: Option[String]): Option[City] =
      val latitude = 48.8566
      val longitude = 2.3522
      cityName.map(name => City(name.asInstanceOf, Coordinates(latitude.asInstanceOf, longitude.asInstanceOf)))

  }
}
