package org.badgrades.wordswithsalt.backend.persistence

import java.time.Instant

import org.badgrades.wordswithsalt.backend.domain.WeatherData

import scala.concurrent.Future

trait WeatherDataRepo {

  def writeWeatherData(weatherData: WeatherData): Future[WeatherData]
  def getMostRecentWeatherData: Future[WeatherData]
  def getWeatherDataByInstant(instant: Instant): Future[WeatherData]
}