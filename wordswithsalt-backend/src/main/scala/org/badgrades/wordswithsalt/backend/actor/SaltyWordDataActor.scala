package org.badgrades.wordswithsalt.backend.actor

import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, Router, SmallestMailboxRoutingLogic}
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor.SaltyWordDataMessage
import org.badgrades.wordswithsalt.backend.domain.SaltyWord

class SaltyWordDataActor extends Actor {
  var router: Router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(SaltyWordFirebaseActor.props)
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case message: SaltyWordDataMessage =>
      router.route(message, sender())

    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(SaltyWordFirebaseActor.props)
      context watch r
      router = router.addRoutee(r)
  }
}

object SaltyWordDataActor {
  def props: Props = Props[SaltyWordDataActor]
  val Name: String = "salty-word-data"

  sealed trait SaltyWordDataMessage
  case class GetWordById(id: String) extends SaltyWordDataMessage
  case class GetWordByPhrase(phrase: String) extends SaltyWordDataMessage
  case class UpdateWordById(id: String) extends SaltyWordDataMessage
  case class WriteWord(saltyWord: SaltyWord) extends SaltyWordDataMessage
  case class FoundWord(id: String, saltyWord: SaltyWord) extends SaltyWordDataMessage
  case class WordNotFound(identifier: Option[String]) extends SaltyWordDataMessage
}
