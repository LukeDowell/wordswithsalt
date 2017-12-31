package org.badgrades.wordswithsalt.backend.service

import java.time.Instant

import org.badgrades.wordswithsalt.backend.domain.RawWeatherData
import org.jsoup.nodes.{Document, Element}

object DocService {

  val DataTableSelector = "table.centeredTable > tbody"

  def parse(document: Document): RawWeatherData = {
    val tableBody: Element = document.selectFirst(DataTableSelector)
    tableBody.children().forEach(tableRow => println(tableRow))

    RawWeatherData(
      timestamp = Instant.now(),
      windSpeed = "",
      maxWindSpeed = "",
      windDirectionInDegrees = "",
      airTemperature = "",
      windChill = "",
      dewPoint = "",
      relativeHumidity = ""
    )
  }
}
