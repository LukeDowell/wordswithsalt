package org.badgrades.wordswithsalt.pruebasgrandes.scenario

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.badgrades.wordswithsalt.pruebasgrandes.request.GetRandomWord

object DefaultLoadTest {
  def apply: ScenarioBuilder = scenario("Default Load Test")
    .exec(GetRandomWord.apply)
}
