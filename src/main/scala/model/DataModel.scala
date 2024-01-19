package model

object DataModel {

  opaque type Latitude = Double
  opaque type Longitude = Double
  opaque type CityName = String
  opaque type Date = String
  opaque type Temperature = Double

  case class Coordinates(latitude: Latitude, longitude: Longitude)

  case class City(name: CityName, coordinates: Coordinates)

  case class CurrentWeather(coordinates: Coordinates,
                            temperature: Temperature,
                            weatherDescription: WeatherStatus,
                            date: Date)

  case class TemperatureRecommendation(temperatureRange: String,
                                        minTemperature: Option[Double],
                                        maxTemperature: Option[Double],
                                        recommendation: String)

  enum WeatherStatus:
    case Sunny, Rainy, Snowy, Cloudy, Clear, PartlyCloudy
}
