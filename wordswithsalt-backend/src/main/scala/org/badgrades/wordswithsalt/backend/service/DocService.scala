package org.badgrades.wordswithsalt.backend.service

import org.badgrades.wordswithsalt.backend.domain.WeatherData
import org.jsoup.nodes.Document

/**
  * A service for parsing weather data from scraped web pages. Each site
  * should have their own implementation of DocService
  */
trait DocService {

  /**
    * Takes in a Jsoup document and parses it into RawWeatherData
    *
    * @param doc The document to parse
    * @return Raw weather data, not converted to legitimate units
    */
  def parse(doc: Document): WeatherData
}
