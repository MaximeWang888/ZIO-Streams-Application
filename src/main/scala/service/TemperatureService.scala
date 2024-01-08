package service

trait TemperatureService {
  def getTemperatureOfCity(cityName: String): CharSequence

  def getRecommandationByCity(cityName: String): CharSequence
}

object TemperatureServiceImpl extends TemperatureService {
    def getTemperatureOfCity(cityName: String): CharSequence =
      s"""La temperature de la ville "$cityName" est de 10°C."""

    def getRecommandationByCity(cityName: String): CharSequence =
      s"""La recommendation selon la température de la ville
         |de "$cityName" est de mettre un t-shirt et des lunettes de soleil.""".stripMargin
}

