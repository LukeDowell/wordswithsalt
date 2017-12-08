package org.badgrades.wordswithsalt.pruebasgrandes.request

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object GetRandomWord {
  def apply: ChainBuilder = exec(http("Get Random Word").get("/word"))
}

object WriteWord {

}