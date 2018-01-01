package org.badgrades.wordswithsalt.backend.service.impl

import java.time.Instant

import org.badgrades.wordswithsalt.backend.domain.RawWeatherData
import org.jsoup.nodes.{Document, Element}

object ChiNoaaDocServiceImpl {

  val DataTableSelector = "table.centeredTable > tbody"

  def parse(document: Document): RawWeatherData = {
    val tableBody: Element = document.select(DataTableSelector).get(1) // Get the second table, the first has no data
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
