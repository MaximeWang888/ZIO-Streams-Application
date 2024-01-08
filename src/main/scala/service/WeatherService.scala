package service

import model.DataModel.City
import model.{DataModel, DataModelDao, DatabaseManager}

trait WeatherService {
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence
}

object WeatherService {
  private val dbManager = new DatabaseManager()
  private val dataModelDao = new DataModelDao(dbManager)

  def getCitiesMatchingWeatherCondition(condition: String): CharSequence = {
    val allCitiesResult = dataModelDao.getAllCities

    val result: CharSequence = allCitiesResult match {
      case Some(cities) =>
        val matchingCities = cities.filter { city =>
          // Assuming you have a method to fetch weather condition for a city
          // Replace `getWeatherConditionForCity` with the actual method you use
          val weatherCondition = dataModelDao.getWeatherConditionForCity(city.name)
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
}
