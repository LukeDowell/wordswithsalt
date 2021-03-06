package org.badgrades.wordswithsalt.backend.persistence.impl

import com.google.api.core.{ApiFuture, ApiFutureCallback, ApiFutures}
import com.google.firebase.database._
import com.typesafe.scalalogging.LazyLogging
import org.badgrades.wordswithsalt.backend.domain.{SaltyWord, SaltyWordBean}
import org.badgrades.wordswithsalt.backend.persistence.SaltyWordRepo

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Random

/**
  * Flushing this down the toilet. RIP Salty Firebase Prince
  */
@Deprecated
class SaltyWordFirebaseImpl(db: FirebaseDatabase) extends SaltyWordRepo with LazyLogging {
  import SaltyWordFirebaseImpl._

  def dbWordRef: DatabaseReference = db.getReference(FirebaseReferencePath)

  override def writeWord(phrase: String, description: String)(implicit ec: ExecutionContext): Future[SaltyWord] = {
    val ref = dbWordRef.push()
    val saltyWord = SaltyWord(ref.getKey, phrase, description)
    logger.info(s"Attempting to write $saltyWord to Firebase")
    ref.setValueAsync(saltyWord.toBean) flatMap { _ =>
      logger.info(s"Successfully wrote $saltyWord to Firebase")
      Future.successful(saltyWord)
    }
  }

  override def getRandomWord(implicit ec: ExecutionContext): Future[SaltyWord] = {
    val p = Promise[SaltyWord]()
    dbWordRef.addValueEventListener(new ValueEventListener {
      def onCancelled(error: DatabaseError): Unit = onCancelledHandler(p, error)
      def onDataChange(snapshot: DataSnapshot): Unit = {
        val words: Seq[SaltyWord] = snapshot
          .getChildren
          .asScala
          .map { childSnapshot: DataSnapshot =>
            val word = childSnapshot.getValue(classOf[SaltyWordBean])
            word.id = childSnapshot.getKey
            word.toCase
          }
          .toSeq

        val randomWord = Random.shuffle(words).headOption
        if (randomWord.isDefined) p.success(randomWord.get)
        else p.failure(new Exception("Unable to find random word"))
      }
    })
    p.future
  }

  override def getWordById(id: String)(implicit ec: ExecutionContext): Future[SaltyWord] = {
    val p = Promise[SaltyWord]()
    dbWordRef.addValueEventListener(new ValueEventListener {
      def onCancelled(error: DatabaseError): Unit = onCancelledHandler(p, error)
      def onDataChange(snapshot: DataSnapshot): Unit = {
        val saltyWord = snapshot.getValue(classOf[SaltyWordBean])
        saltyWord.id = snapshot.getKey
        logger.info(s"SaltyWord retrieved ${saltyWord.toCase}")
        p.success(saltyWord.toCase)
      }
    })
    p.future
  }

  override def getAllWords(implicit ec: ExecutionContext): Future[Seq[SaltyWord]] = {
    val p = Promise[Seq[SaltyWord]]
    dbWordRef.addValueEventListener(new ValueEventListener {
      def onCancelled(error: DatabaseError): Unit = onCancelledHandler(p, error)
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
        p.success(words)
      }
    })
    p.future
  }


  private[persistence] def onCancelledHandler[T](p: Promise[T], error: DatabaseError): Unit = {
    logger.error(s"Error reading from Firebase. Code=${error.getCode}, Message=${error.getMessage}, Details=${error.getDetails}")
    p.failure(new Exception(error.getMessage))
  }
}

object SaltyWordFirebaseImpl {
  val FirebaseReferencePath = "/words"

  implicit def firebaseFutureToScalaFuture[T](apiFuture: ApiFuture[T])(implicit ec: ExecutionContext): Future[T] = {
    val promise = Promise[T]()
    ApiFutures.addCallback(
      apiFuture,
      new ApiFutureCallback[T] {
        override def onFailure(t: Throwable): Unit = promise.failure(t)
        override def onSuccess(result: T): Unit = promise.success(result)
      }
    )
    promise.future
  }
}