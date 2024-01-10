package service

import model.DataModel.Coordinates
import model.{DataModel, DataModelDao, DatabaseManager}
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.http.Method
import zio.http.Client
import zio.Console

trait TemperatureService {
  def getTemperatureOfCity(cityName: String): CharSequence

  def getRecommandationByCity(cityName: String): CharSequence
}

object TemperatureService {
    private val dbManager = new DatabaseManager()
    private val dataModelDao = new DataModelDao(dbManager)
    private final val BASE_URL = "https://api.openweathermap.org/data/2.5/weather"
    private final val API_KEY = "d1973fd69722dbf4497b5701fb60f939" 
  
    def getTemperatureOfCity(cityName: String): CharSequence = {
        val cityOption  = dataModelDao.getCityToCoord(cityName)

        val result: CharSequence = cityOption match {
          case Some(city) =>
            val temperatureOption = getTemperatureFromCoord(city.coordinates)

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
          val temperatureOption = getTemperatureFromCoord(city.coordinates)
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

  def getTemperatureFromCoord(coordinates: Coordinates): Option[DataModel.Temperature] = {
    val queryParams = s"lat=${coordinates.latitude}&lon=${coordinates.longitude}&appid=$API_KEY"

    val response = for {
      getRes <- Client.request(
        s"$BASE_URL?$queryParams",
        Method.GET,
      )
      gotData <- getRes.body.asString
      _ <- Console.printLine(s"gotData: $gotData")
    } yield ()

    response match {
      case Right(body) =>
        val temperature = parseTemperature(body.asInstanceOf[String])
        Some(temperature.asInstanceOf[DataModel.Temperature])
      case Left(error) =>
        println(s"Error making API request: $error")
        None
    }
  }

  def parseTemperature(responseBody: String): Option[DataModel.Temperature] = {
    
    // TODO implement this func
    
  }
}

