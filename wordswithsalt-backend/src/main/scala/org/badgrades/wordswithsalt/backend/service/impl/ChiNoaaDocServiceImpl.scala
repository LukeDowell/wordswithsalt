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

  /**
    * Extension methods for an element that contains a list of <tr> elements with title and
    * value child elements
    */
  private[impl] implicit class ChiNoaaTableExtensions(el: Element) {

    /**
      * Returns an option with the first element node that has a "<td>" element that matches the provided text, or None if not found
      * @param text The text to match for
      */
    def findRowWithTitle(text: String): Option[Element] = Option(el.selectFirst(s"tr > td:contains($text)"))

    /**
      * Parses a "<tr>" element with two child "<td>" elements.
      * The first will have a title and the second will have a value.
      */
    def readRowValue(): Option[String] = Option(el.children().last().text().replace("&nbsp", ""))
  }
}
