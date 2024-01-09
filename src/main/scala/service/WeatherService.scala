package service

import model.DataModel.City
import model.{DataModel, DataModelDao, DatabaseManager}

trait WeatherService {
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence
}

object WeatherService {

  def getCitiesMatchingWeatherCondition(condition: String): CharSequence = {
    val dbManager = new DatabaseManager()
    val dataModelDao = new DataModelDao(dbManager)
    val allCitiesResult = dataModelDao.getAllCities

    val result: CharSequence = allCitiesResult match {
      case Some(cities) =>
        val matchingCities = cities.filter { city =>
          // Assuming you have a method to fetch weather condition for a city
          // Replace `getWeatherConditionForCity` with the actual method you use
          val weatherCondition = getWeatherConditionForCity(city.name)
          weatherCondition.equalsIgnoreCase(condition)
        }

        if (matchingCities.nonEmpty) {
          matchingCities.mkString(", ") 
        } else {
          "No cities match the specified weather condition."
        }

      case None => "Failed to retrieve cities data."
    }

    result
  }

  // Example method, replace it with your actual implementation
  private def getWeatherConditionForCity(cityName: DataModel.CityName): String = {
    // Implement logic to fetch weather condition for the given city
    // This is just a placeholder, replace it with your actual logic
    "Sunny"
  }
}
