package org.badgrades.wordswithsalt.backend.service

import java.time.Instant

import org.badgrades.wordswithsalt.backend.domain.WeatherData
import org.jsoup.nodes.Document

object DocService {

  def parse(document: Document): WeatherData = {
    WeatherData(Instant.now())
  }
}
