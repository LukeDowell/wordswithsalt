package org.badgrades.wordswithsalt.backend.service.impl

import java.time.Instant

import org.badgrades.wordswithsalt.backend.domain.WeatherData
import org.jsoup.nodes.{Document, Element}

object ChiNoaaDocServiceImpl {
  def parse(document: Document): WeatherData = {
    val tableBody: Element = document.select("table.centeredTable > tbody").get(1) // Get the second table, the first is just an un-parsable wrapper
    def parseRowWithTitle(title: String) = tableBody.findRowWithTitle(title)
      .flatMap(row => row.readRowValue())
      .map(rowValue => rowValue.replace("&nbsp", ""))
      .getOrElse("")

    WeatherData(
      timestamp = Instant.now(),
      windSpeed = parseRowWithTitle("Wind Speed:"),
      maxWindSpeed = parseRowWithTitle("Max Wind Speed:"),
      windDirectionInDegrees = parseRowWithTitle("Wind Direction:"),
      airTemperature = parseRowWithTitle("Air Temperature:"),
      windChill = parseRowWithTitle("Wind Chill:"),
      dewPoint = parseRowWithTitle("Dew Point:"),
      relativeHumidity = parseRowWithTitle("Relative Humidity:")
    )
  }

  /**
    * Extension methods for an element that contains a list of <tr> elements with title and
    * value child elements
    */
  private[impl] implicit class ChiNoaaTableExtensions(el: Element) {

    /**
      * Returns an option of the first <tr> element that has a child <td> element containing the provided text.
      * @param text The text to match for
      */
    def findRowWithTitle(text: String): Option[Element] = Option(el.selectFirst(s"tr:has(td:contains($text))"))

    /**
      * Parses a <tr> element with two child <td> elements.
      * The first will have a title and the second will have a value, which is the value that is returned.
      */
    def readRowValue(): Option[String] = Option(el.children().last().text())
  }
}
