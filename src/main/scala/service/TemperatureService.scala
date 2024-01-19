package service

import model.{DataModel, DataModelDao, DatabaseManager}

// Define the TemperatureService trait with two methods
trait TemperatureService {
  def getTemperatureOfCity(cityName: String): CharSequence

  def getRecommandationByCity(cityName: String): CharSequence
}

object TemperatureService {
  // Initialize database-related components
  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)

  // Implementation of the TemperatureService trait
  def getTemperatureOfCity(cityName: String): CharSequence = {
    // Retrieve coordinates of the city from the database
    val cityOption = dataModelDao.getCityToCoord(cityName)

    // Process the result based on the retrieved data
    val result: CharSequence = cityOption match {
      case Some(city) =>
        // If city information is found, retrieve the temperature for the city
        val temperatureOption = dataModelDao.getTemperatureFromCoord(city.coordinates)

        temperatureOption match {
          case Some(temperatureOfCity) =>
            // If temperature data is found, create a response with the temperature
            s"The temperature in $cityName is ${temperatureOfCity} degrees Celsius."
          case None =>
            // If temperature data is not found, indicate failure to retrieve temperature data
            "Failed to retrieve temperature data for the city."
        }

      case None =>
        // If city information is not found, indicate failure to retrieve city data
        "Failed to retrieve cities data."
    }

    result
  }

  def getRecommandationByCity(cityName: String): CharSequence = {
    // Retrieve coordinates of the city from the database
    val cityOption = dataModelDao.getCityToCoord(cityName)

    // Process the result based on the retrieved data
    val result: CharSequence = cityOption match {
      case Some(city) =>
        // If city information is found, retrieve the temperature for the city
        val temperatureOption = dataModelDao.getTemperatureFromCoord(city.coordinates)

        temperatureOption match {
          case Some(temperatureOfCity) =>
            // If temperature data is found, retrieve the recommendation for the city
            val recommendationOption = dataModelDao.getCityRecommendation(temperatureOfCity)

            recommendationOption match {
              case Some(recommendation) =>
                // If recommendation data is found, create a response with the recommendation
                s"The recommendation for the city of $cityName is: ${recommendation.recommendation}"
              case None =>
                // If recommendation data is not found, indicate failure to retrieve recommendation data
                "Failed to retrieve recommendation data for the city."
            }
          case None =>
            // If temperature data is not found, indicate failure to retrieve temperature data
            "Failed to retrieve temperature data for the city."
        }
      case None =>
        // If city information is not found, indicate failure to retrieve city data
        "Failed to retrieve cities data."
    }
    result
  }
}
