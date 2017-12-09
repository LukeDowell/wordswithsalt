package org.badgrades.wordswithsalt.pruebasgrandes.request

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

case object PostSaltyWord {
  def apply(phrase: String = "", description: String = ""): ChainBuilder = exec(
    http("Write Word")
      .post("/word")
      .queryParamMap(Map(
        "phrase" -> phrase,
        "description" -> description
      ))
  )
}
