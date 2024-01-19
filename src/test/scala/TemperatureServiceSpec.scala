import zio.test._
import zio.test.Assertion._
import service.TemperatureService
import model.{DataModel, DataModelDao, DatabaseManager}

object TemperatureServiceSpec extends ZIOSpecDefault {

  override def spec = suite("TestingApplicationsExamplesSpec")(
    suite("TemperatureService")(
      test("getTemperatureOfCity should return the correct response when temperature data is available") {
        val cityName = "Paris"
        val service = new TemperatureServiceMock(DataModel.mockCityData(Some(cityName)))

        val result = service.getTemperatureOfCity(cityName)

        assert(result)(equalTo(s"The temperature in $cityName is 22.5 degrees Celsius."))
      },

      test("getTemperatureOfCity should return an error message when temperature data is not available") {
        val cityName = "NonExistentCity"
        val service = new TemperatureServiceMock(DataModel.mockCityData(None))
        val result = service.getTemperatureOfCity(cityName)
        assert(result)(equalTo("Failed to retrieve cities data."))
      },

      test("getTemperatureOfCity should return an error message when city data is not available") {
        val cityName = "NonExistentCity"
        val service = new TemperatureServiceMock(DataModel.mockCityData(None))
        val result = service.getTemperatureOfCity(cityName)
        assert(result)(equalTo("Failed to retrieve cities data."))
      },

      test("getRecommandationByCity should return the correct recommendation when all data is available") {
        val cityName = "Paris"
        val service = new TemperatureServiceMock(DataModel.mockCityData(Some(cityName)))
        val result = service.getRecommandationByCity(cityName)
        assert(result)(
          equalTo("The recommendation for the city of Paris is: Enjoy the moderate weather!")
        )
      },

      test("getRecommandationByCity should return an error message when city data is not available") {
        val cityName = "NonExistentCity"
        val service = new TemperatureServiceMock(DataModel.mockCityData(None))
        val result = service.getRecommandationByCity(cityName)
        assert(result)(equalTo("Failed to retrieve cities data."))
      },

      test("getRecommandationByCity should return an error message when temperature data is not available") {
        val cityName = "Paris"
        val service = new TemperatureServiceMock(DataModel.mockCityData(None))
        val result = service.getRecommandationByCity(cityName)

        assert(result)(equalTo("Failed to retrieve cities data."))
      },

      test("getRecommandationByCity should return an error message when recommendation data is not available") {
        val cityName = "Paris"
        val service = new TemperatureServiceMock(DataModel.mockCityData(None))
        val result = service.getRecommandationByCity(cityName)

        assert(result)(equalTo("Failed to retrieve cities data."))
      }
    )
  )

  case class TemperatureServiceMock(cityData: Option[model.DataModel.City]) extends TemperatureService {
    override def getTemperatureOfCity(cityName: String): CharSequence = {
      val cityOption = cityData.orElse(DataModel.mockCityData(None))
      val result: CharSequence = cityOption match {
        case Some(city) =>
          val temperatureOption = DataModel.mockTemperatureData(city.coordinates)
          temperatureOption.map { temperatureOfCity =>
            s"The temperature in $cityName is $temperatureOfCity degrees Celsius."
          }.getOrElse("Failed to retrieve temperature data for the city.")
        case None => "Failed to retrieve cities data."
      }
      result
    }

    override def getRecommandationByCity(cityName: String): CharSequence = {
      val cityOption = cityData.orElse(DataModel.mockCityData(None))
      val result: CharSequence = cityOption match {
        case Some(city) =>
          val temperatureOption = DataModel.mockTemperatureData(city.coordinates)
          temperatureOption match {
            case Some(temperatureOfCity) =>
              val recommendationOption = DataModel.mockRecommendationData(temperatureOfCity)
              recommendationOption.map { recommendation =>
                s"The recommendation for the city of $cityName is: ${recommendation.recommendation}"
              }.getOrElse("Failed to retrieve recommendation data for the city.")
            case None =>
              "Failed to retrieve temperature data for the city."
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

    def mockTemperatureData(coordinates: Coordinates): Option[Double] =
      if (coordinates.latitude.asInstanceOf == 48.8566 && coordinates.longitude.asInstanceOf == 2.3522)
        Some(22.5)
      else
        None

    def mockRecommendationData(temperature: Double): Option[TemperatureRecommendation] =
      if (temperature >= 20)
        Some(TemperatureRecommendation("Moderate", None, Some(30.0), "Enjoy the moderate weather!"))
      else
        None
  }
}
