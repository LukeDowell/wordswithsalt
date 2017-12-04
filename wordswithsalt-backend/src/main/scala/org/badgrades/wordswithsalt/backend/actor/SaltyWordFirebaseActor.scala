package org.badgrades.wordswithsalt.backend.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.google.firebase.database._
import org.badgrades.wordswithsalt.backend.FirebaseConfig
import org.badgrades.wordswithsalt.backend.domain.{SaltyWord, SaltyWordBean}
import org.badgrades.wordswithsalt.backend.util.FirebaseExtensions._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Random, Success}

class SaltyWordFirebaseActor(val db: FirebaseDatabase) extends Actor with ActorLogging {
  import SaltyWordDataActor._
  import SaltyWordFirebaseActor._

  implicit val ec: ExecutionContext = context.dispatcher
  def dbWordRef: DatabaseReference = db.getReference(FirebaseReferencePath)

  override def receive: Receive = {
    case WriteWord(phrase, description) =>
      log.info(s"Attempting to write word with phrase=$phrase, description=$description")
      val ref = dbWordRef.push()
      val saltyWord = SaltyWord(ref.getKey, phrase, description)
      ref.setValueAsync(saltyWord) onComplete {
        case Success(_) => log.info(s"$saltyWord successfully written to DB!")
        case Failure(err) => log.error(s"Error while writing $saltyWord to DB.", err)
      }

    case GetRandomWord =>
      log.info("Attempting to retrieve random word")
      val replyTo = sender()
      dbWordRef.addValueEventListener(new ValueEventListener {
        def onCancelled(error: DatabaseError): Unit = {
          log.error(s"Error reading from Firebase. Code=${error.getCode}, Message=${error.getMessage}, Details=${error.getDetails}")
          replyTo ! FirebaseError(error.getMessage)
        }
        def onDataChange(snapshot: DataSnapshot): Unit = {
          val words: Seq[SaltyWord] = snapshot
            .getChildren
            .asScala
            .map {
              childSnapshot: DataSnapshot =>
                val word = childSnapshot.getValue(classOf[SaltyWordBean])
                word.id = childSnapshot.getKey
                word.toCase
            }
            .toSeq

          val randomWord = Random.shuffle(words).headOption
          if (randomWord.isDefined) replyTo ! FoundWord(randomWord.get)
          else replyTo ! WordNotFound
        }
      })

    case GetAllWords =>
      log.info("Attempting to retrieve all words")
      val replyTo = sender()
      dbWordRef.addValueEventListener(new ValueEventListener {
        def onCancelled(error: DatabaseError): Unit = {
          log.error(s"Error reading from Firebase. Code=${error.getCode}, Message=${error.getMessage}, Details=${error.getDetails}")
          replyTo ! FirebaseError(error.getMessage)
        }
        def onDataChange(snapshot: DataSnapshot): Unit = {
          val words: Seq[SaltyWord] = snapshot
            .getChildren
            .asScala
            .map {
              childSnapshot: DataSnapshot =>
                val word = childSnapshot.getValue(classOf[SaltyWordBean])
                word.id = childSnapshot.getKey
                word.toCase
            }
            .toSeq
          replyTo ! FoundAllWords(words)
        }
      })

    case GetWordById(id) =>
      log.info(s"Attempting to retrieve word with id=$id")
      val replyTo = sender()
      dbWordRef.addValueEventListener(new ValueEventListener {
        def onCancelled(error: DatabaseError): Unit = {
          log.error(s"Error reading from Firebase. Code=${error.getCode}, Message=${error.getMessage}, Details=${error.getDetails}")
          replyTo ! FirebaseError(error.getMessage)
        }
        def onDataChange(snapshot: DataSnapshot): Unit = {
          val saltyWord = snapshot.getValue(classOf[SaltyWordBean])
          saltyWord.id = snapshot.getKey
          log.info(s"SaltyWord retrieved ${saltyWord.toCase}")
          replyTo ! FoundWord(saltyWord.toCase)
        }
      })

    case UpdateWordById(id) =>
      log.info(s"Attempting to update word with id=$id")
  }
}

object SaltyWordFirebaseActor {
  def props(database: FirebaseDatabase = FirebaseDatabase.getInstance(FirebaseConfig.app)): Props =
    Props(new SaltyWordFirebaseActor(database))

  val FirebaseReferencePath = "/words"

  case class FirebaseError(error: String)
}
