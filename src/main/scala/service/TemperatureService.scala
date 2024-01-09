package service

import model.{DataModel, DataModelDao, DatabaseManager}

trait TemperatureService {
  def getTemperatureOfCity(cityName: String): CharSequence

  def getRecommandationByCity(cityName: String): CharSequence
}

object TemperatureService {
    private val dbManager = new DatabaseManager()
    private val dataModelDao = new DataModelDao(dbManager)
    def getTemperatureOfCity(cityName: String): CharSequence = {
        val cityOption  = dataModelDao.getCityToCoord(cityName)

        val result: CharSequence = cityOption match {
          case Some(city) =>
            val temperatureOption = dataModelDao.getTemperatureFromCoord(city.coordinates)

            temperatureOption match {
              case Some(temperatureOfCity) =>
                s"The temperature in $cityName is ${temperatureOfCity} degrees Celsius."
              case None =>
                "Failed to retrieve temperature data for the city."
            }

          case None => "Failed to retrieve cities data."
        }

        result
      }

    def getRecommandationByCity(cityName: String): CharSequence = {
      val cityOption = dataModelDao.getCityToCoord(cityName)

      val result: CharSequence = cityOption match {
        case Some(city) =>
          val temperatureOption = dataModelDao.getTemperatureFromCoord(city.coordinates)
          temperatureOption match {
            case Some(temperatureOfCity) =>
              val recommendationOption = dataModelDao.getCityRecommendation(temperatureOfCity)

              recommendationOption match {
                case Some(recommendation) =>
                  s"The recommendation for the city of $cityName is: ${recommendation.recommendation}"
                case None =>
                  "Failed to retrieve recommendation data for the city."
              }
            case None =>
              "Failed to retrieve temperature data for the city."
          }
        case None => "Failed to retrieve cities data."
      }
      
      result
    }

}

