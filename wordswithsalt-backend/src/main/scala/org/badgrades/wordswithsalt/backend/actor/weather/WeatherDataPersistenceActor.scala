package org.badgrades.wordswithsalt.backend.actor.weather

import akka.actor.{Actor, ActorLogging, Props}
import org.badgrades.wordswithsalt.backend.domain.WeatherData

class WeatherDataPersistenceActor extends Actor with ActorLogging {
  import WeatherDataPersistenceActor._

  override def receive: Receive = {
    case WriteWeatherData(weatherData) =>
      log.info(s"Persisting $weatherData")
  }
}

object WeatherDataPersistenceActor {
  def props: Props = Props[WeatherDataPersistenceActor]
  case class WriteWeatherData(weatherData: WeatherData)
}