package org.badgrades.wordswithsalt.backend.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.google.firebase.database._
import org.badgrades.wordswithsalt.backend.FirebaseConfig
import org.badgrades.wordswithsalt.backend.actor.SaltyWordFirebaseActor.FirebaseError
import org.badgrades.wordswithsalt.backend.domain.SaltyWordBean
import org.badgrades.wordswithsalt.backend.util.FirebaseExtensions._

import scala.concurrent.ExecutionContext

class SaltyWordFirebaseActor extends Actor with ActorLogging {
  import SaltyWordDataActor._

  implicit val ec: ExecutionContext = context.dispatcher
  val db: FirebaseDatabase = FirebaseDatabase.getInstance(FirebaseConfig.app)
  val dbWordRef: DatabaseReference = db.getReference("/words")

  override def receive: Receive = {
    case WriteWord(saltyWord) =>
      log.info(s"Attempting to write word $saltyWord...")
      val ref = dbWordRef.push()
      ref.setValueAsync(saltyWord.toBean) map { result =>
        log.info(s"Finished writing with id=${ref.getKey}")
      }

    case GetWordById(id) =>
      log.info(s"Attempting to retrieve word with id=$id")
      val replyTo = sender()
      dbWordRef.child(id).addValueEventListener(saltyWordValueHandler(replyTo))

    case UpdateWordById(id) =>
      log.info(s"Attempting to update word with id=$id")
  }

  def saltyWordValueHandler(replyTo: ActorRef): ValueEventListener = new ValueEventListener {
    override def onCancelled(error: DatabaseError): Unit = {
      log.error(s"Error reading from Firebase. Code=${error.getCode}, Message=${error.getMessage}, Details=${error.getDetails}")
      replyTo ! FirebaseError(error.getMessage)
    }
    override def onDataChange(snapshot: DataSnapshot): Unit = {
      val saltyWord = snapshot.getValue(classOf[SaltyWordBean])
      log.info(s"SaltyWord retrieved $saltyWord")
      replyTo ! FoundWord(snapshot.getKey, saltyWord.toCase)
    }
  }
}

object SaltyWordFirebaseActor {
  def props: Props = Props[SaltyWordFirebaseActor]

  case class FirebaseError(error: String)
}
