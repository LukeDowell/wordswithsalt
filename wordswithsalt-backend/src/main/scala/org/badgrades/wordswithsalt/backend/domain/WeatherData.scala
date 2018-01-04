package org.badgrades.wordswithsalt.backend.domain

import java.time.Instant

/**
  *
  * @param timestamp The time this data was recorded
  * @param windSpeed The current wind speed at the time of measurement
  * @param maxWindSpeed The max wind speed for that day
  * @param windDirectionInDegrees The wind direction in degrees
  * @param airTemperature The air temperature at the time of measurement
  * @param windChill The wind chill at the time of measurement
  * @param dewPoint The dew point at the time of measurement
  * @param relativeHumidity The relative humidity at the time of measurement
  */
case class WeatherData(
                        timestamp: Instant = Instant.now(),
                        windSpeed: String,
                        maxWindSpeed: String,
                        windDirectionInDegrees: String,
                        airTemperature: String,
                        windChill: String,
                        dewPoint: String,
                        relativeHumidity: String
                      )