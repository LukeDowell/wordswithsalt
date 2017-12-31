package org.badgrades.wordswithsalt.backend.service

import java.io.File

import org.badgrades.wordswithsalt.backend.config.Constants
import org.badgrades.wordswithsalt.backend.domain.RawWeatherData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.WordSpec

class DocServiceSpec extends WordSpec {

  val testSiteFile = new File(classOf[DocServiceSpec].getResource("weather-site.html").toURI)

  "A DocService" must {
    val doc: Document = Jsoup.parse(testSiteFile, "UTF-8", Constants.WeatherDataUrl)

    "parse a web page into valid weather data" in {
      val rawWeatherData: RawWeatherData = DocService.parse(doc)
    }
  }
}
