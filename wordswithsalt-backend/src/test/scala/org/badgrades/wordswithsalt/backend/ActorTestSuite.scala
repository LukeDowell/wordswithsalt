package org.badgrades.wordswithsalt.backend

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, TestSuite}

/**
  * This class provides a convenient place to mock out common systems needed by any
  * actors in our application. It also allows us to use whatever testing spec we would like,
  * simply mixin the desired spec. Example: WordSpecLike, FunSpecLike, etc.
  */
abstract class ActorTestSuite extends TestKit(ActorSystem("testSystem", ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with TestSuite
  with ImplicitSender
  with BeforeAndAfterAll {

  override def afterAll {
    system.terminate()
    super.afterAll()
  }
}