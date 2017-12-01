package org.badgrades.wordswithsalt.backend.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.google.firebase.database.{DatabaseReference, FirebaseDatabase}
import org.badgrades.wordswithsalt.backend.FirebaseConfig
import org.badgrades.wordswithsalt.backend.domain.SaltyWord
import org.badgrades.wordswithsalt.backend.util.FirebaseConversions._

import scala.concurrent.ExecutionContext

class SaltyWordDataActor extends Actor with ActorLogging {
  import SaltyWordDataActor._

  val db: FirebaseDatabase = FirebaseDatabase.getInstance(FirebaseConfig.app)
  val dbRef: DatabaseReference = db.getReference("/words").push()
  implicit val ec: ExecutionContext = context.dispatcher

  override def receive: Receive = {
    case WriteWord(saltyWord) =>
      log.info(s"Attempting to write word $saltyWord...")
      dbRef.setValueAsync(saltyWord.toBean) map { result =>
        log.info(s"Finished writing with result=$result")
      }
      context stop self
  }
}

object SaltyWordDataActor {
  def props: Props = Props[SaltyWordDataActor]

  case class GetWordByIdentifier(identifier: Either[Long, String])
  case class WriteWord(saltyWord: SaltyWord)
  case class FoundWord(id: String, saltyWord: SaltyWord)
}
