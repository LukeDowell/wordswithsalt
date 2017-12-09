package org.badgrades.wordswithsalt.pruebasgrandes.scenario

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.badgrades.wordswithsalt.pruebasgrandes.request.{GetRandomSaltyWord, PostSaltyWord}

import scala.util.Random

object DefaultLoadTest {
  def apply: ScenarioBuilder = {
    scenario("Default Load Test")
      .exec(
        GetRandomSaltyWord(),
        PostSaltyWord(randomString(), randomString())
      )
  }

  def randomString(length: Int = 5): String = Random
    .alphanumeric
    .take(length)
    .mkString
}
