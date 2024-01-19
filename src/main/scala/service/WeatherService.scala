package service

import model.DataModel.City
import model.{DataModel, DataModelDao, DatabaseManager}

trait WeatherService {
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence
}

object WeatherService {
  // Initialize a DatabaseManager object and DataModelDao for data management
  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)

  // Method that returns a character sequence corresponding to cities with a specified weather condition
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence = {
    // Retrieve all cities from the database
    val allCitiesResult = dataModelDao.getAllCities

    // Use pattern matching to handle cases where city retrieval succeeds or fails
    val result: CharSequence = allCitiesResult match {
      case Some(cities) =>
        // Filter cities that match the specified weather condition
        val matchingCities = cities.filter { city =>
          val weatherCondition = dataModelDao.getWeatherConditionForCity(city.name)
          weatherCondition.equalsIgnoreCase(condition)
        }

        // Check if there are cities matching the weather condition
        if (matchingCities.nonEmpty) {
          // Create a character sequence with the names of the matching cities
          val cityNames = matchingCities.map(_.name)
          cityNames.mkString(", ")
        } else {
          // Message in case there are no cities matching the specified weather condition
          "No cities match the specified weather condition."
        }

      case None => "Failed to retrieve cities data."  // Message in case of failure to retrieve city data
    }

    // Return the resulting character sequence
    result
  }
}
