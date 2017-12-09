package org.badgrades.wordswithsalt.pruebasgrandes.simulation

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.badgrades.wordswithsalt.pruebasgrandes.scenario.DefaultLoadTest

import scala.concurrent.duration._
import scala.language.postfixOps

class DefaultLoadSimulation extends Simulation {
  val httpConf: HttpProtocolBuilder = http
    .baseURL("http://localhost:6969")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")

  setUp(
    DefaultLoadTest.apply.inject(rampUsers(500) over (30 seconds))
  ).protocols(httpConf)
}
