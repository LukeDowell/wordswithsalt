package org.badgrades.wordswithsalt.backend.actor.word

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import org.badgrades.wordswithsalt.backend.domain.SaltyWord

class SaltyWordDataActor extends Actor with ActorLogging {
  import SaltyWordDataActor._

  var router: Router = {
    val routees = Vector.fill(NumRoutees) {
      val r = context.actorOf(SaltyWordFirebaseActor.props())
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case message: SaltyWordDataMessage =>
      router.route(message, sender())

    case Terminated(a) =>
      log.warning(s"Actor=${a.path} terminated, replacing...")
      router = router.removeRoutee(a)
      val r = context.actorOf(SaltyWordFirebaseActor.props())
      context watch r
      router = router.addRoutee(r)
  }

  override def preStart(): Unit = log.info(s"SaltyWordDataActor spinning up with $NumRoutees routees")
}

object SaltyWordDataActor {
  def props: Props = Props[SaltyWordDataActor]
  val Name: String = "salty-word-data-actor"
  val NumRoutees: Int = 5

  sealed trait SaltyWordDataMessage
  case class GetWordById(id: String) extends SaltyWordDataMessage
  case object GetAllWords extends SaltyWordDataMessage
  case object GetRandomWord extends SaltyWordDataMessage
  case class UpdateWordById(id: String) extends SaltyWordDataMessage
  case class WriteWord(phrase: String, description: String) extends SaltyWordDataMessage
  case class FoundWord(saltyWord: SaltyWord) extends SaltyWordDataMessage
  case class FoundAllWords(saltyWords: Seq[SaltyWord])
  case class WordNotFound(identifier: Option[String]) extends SaltyWordDataMessage
}
