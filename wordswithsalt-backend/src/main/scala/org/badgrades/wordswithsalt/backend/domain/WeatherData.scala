package org.badgrades.wordswithsalt.backend.domain

import java.time.Instant

/**
  *
  * @param timestamp
  * @param windSpeed
  * @param maxWindSpeed
  * @param windDirectionInDegrees
  * @param airTemperature
  * @param windChill
  * @param dewPoint
  * @param relativeHumidity
  */
case class RawWeatherData(
                           timestamp: Instant = Instant.now(),
                           windSpeed: String,
                           maxWindSpeed: String,
                           windDirectionInDegrees: String,
                           airTemperature: String,
                           windChill: String,
                           dewPoint: String,
                           relativeHumidity: String
                         )