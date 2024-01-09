package model

object DataModel {

  opaque type Latitude = Double
  opaque type Longitude = Double
  opaque type CityName = String
  opaque type Date = String

  case class Coordinates(latitude: Latitude, longitude: Longitude)

  case class City(name: CityName, coordinates: Coordinates)

  case class CurrentWeather(coordinates: Coordinates,
                            temperature: Double,
                            weatherDescription: String,
                            date: Date)

  case class TemperatureRecommendation(temperatureRange: String, recommendation: String)
  
  enum WeatherStatus:
    case Sun, Rain, Snow
}
