package org.badgrades.wordswithsalt.backend.actor.word

import akka.actor.{Actor, ActorLogging, Props}
import com.google.firebase.database._
import org.badgrades.wordswithsalt.backend.FirebaseConfig
import org.badgrades.wordswithsalt.backend.persistence.SaltyWordRepo
import org.badgrades.wordswithsalt.backend.persistence.impl.SaltyWordFirebaseImpl

import scala.concurrent.ExecutionContext


class SaltyWordFirebaseActor(val db: FirebaseDatabase) extends Actor with ActorLogging {
  import SaltyWordActor._

  implicit val ec: ExecutionContext = context.dispatcher
  val saltyWordRepo: SaltyWordRepo = new SaltyWordFirebaseImpl(db)

  override def receive: Receive = {
    case WriteWord(phrase, description) =>
      log.info(s"Attempting to write word with phrase=$phrase, description=$description")
      saltyWordRepo.writeWord(phrase, description)

    case GetRandomWord =>
      log.info("Attempting to retrieve random word")
      val replyTo = sender()
      saltyWordRepo.getRandomWord map { word => replyTo ! FoundWord(word) }

    case GetAllWords =>
      log.info("Attempting to retrieve all words")
      val replyTo = sender()
      saltyWordRepo.getAllWords map { words => replyTo ! FoundAllWords(words) }

    case GetWordById(id) =>
      log.info(s"Attempting to retrieve word with id=$id")
      val replyTo = sender()
      saltyWordRepo.getWordById(id) map { word => replyTo ! FoundWord(word) }
  }
}

object SaltyWordFirebaseActor {
  def props(database: FirebaseDatabase = FirebaseDatabase.getInstance(FirebaseConfig.app)): Props =
    Props(new SaltyWordFirebaseActor(database))

  case class FirebaseError(error: String)
}
