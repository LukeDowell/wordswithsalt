package org.badgrades.wordswithsalt.backend.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.google.firebase.database.FirebaseDatabase
import org.badgrades.wordswithsalt.backend.FirebaseConfig
import org.badgrades.wordswithsalt.backend.util.FirebaseConversions._

import scala.concurrent.ExecutionContext

class SaltyWordFirebaseActor extends Actor with ActorLogging {
  import SaltyWordDataActor._

  val db: FirebaseDatabase = FirebaseDatabase.getInstance(FirebaseConfig.app)
  implicit val ec: ExecutionContext = context.dispatcher

  override def receive: Receive = {
    case WriteWord(saltyWord) =>
      log.info(s"Attempting to write word $saltyWord...")
      val dbRef = db.getReference("/words").push()
      dbRef.setValueAsync(saltyWord.toBean) map { result =>
        log.info(s"Finished writing with id=${dbRef.getKey}")
      }

    case GetWordById(id) =>
      log.info(s"Attempting to retrieve word with id=$id")

    case GetWordByPhrase(phrase) =>
      log.info(s"Attempting to retrieve word with phrase=$phrase")

    case UpdateWordById(id) =>
      log.info(s"Attempting to update word with id=$id")
  }
}

object SaltyWordFirebaseActor {
  def props: Props = Props[SaltyWordFirebaseActor]
}
