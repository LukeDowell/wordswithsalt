package org.badgrades.wordswithsalt.backend.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.badgrades.wordswithsalt.backend.domain.SaltyWord
import spray.json.RootJsonFormat

trait JsonSupport extends SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit val saltyWordFormat: RootJsonFormat[SaltyWord] = jsonFormat3(SaltyWord)
}
