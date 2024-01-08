package service

trait WeatherService {
  def getCitiesMatchingWeatherCondition(condition: String): CharSequence
}

object WeatherService {

  def getCitiesMatchingWeatherCondition(condition: String): CharSequence =
    s"""Les villes ayant de la "$condition" sont Paris, Boulogne, Villejuif."""

}
