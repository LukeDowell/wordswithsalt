package org.badgrades.wordswithsalt.backend.persistence

import java.time.Instant

import org.badgrades.wordswithsalt.backend.domain.RawWeatherData

import scala.concurrent.Future

trait WeatherDataRepo {

  def writeWeatherData(data: RawWeatherData): Future[RawWeatherData]
  def getMostRecentWeatherData: Future[RawWeatherData]
  def getWeatherDataByInstant(instant: Instant): Future[RawWeatherData]
}
