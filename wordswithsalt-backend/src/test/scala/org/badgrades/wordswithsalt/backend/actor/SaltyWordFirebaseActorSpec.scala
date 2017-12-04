package org.badgrades.wordswithsalt.backend.actor

import akka.actor.ActorRef
import com.google.firebase.database.{DataSnapshot, DatabaseReference, FirebaseDatabase}
import org.badgrades.wordswithsalt.backend.ActorTestSuite
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor.{FoundWord, GetRandomWord}
import org.badgrades.wordswithsalt.backend.domain.SaltyWord
import org.mockito.Matchers._
import org.mockito.{Mock, Mockito}
import org.mockito.Mockito._
import org.scalatest.WordSpecLike

import scala.concurrent.duration._
import scala.collection.JavaConverters._
import scala.language.postfixOps
import scala.reflect.ClassTag

class SaltyWordFirebaseActorSpec extends ActorTestSuite with WordSpecLike {

  val firebaseDatabaseMock: FirebaseDatabase = Mockito.mock(classOf[FirebaseDatabase])
  val dbRefMock: DatabaseReference = Mockito.mock(classOf[DatabaseReference])

  "A SaltyWordFirebaseActor" must {
    when(firebaseDatabaseMock.getReference(SaltyWordFirebaseActor.FirebaseReferencePath)).thenReturn(dbRefMock)
    val subjectRef: ActorRef = system.actorOf(SaltyWordFirebaseActor.props(firebaseDatabaseMock))

    "respond with a random word" in {
      val wordSet: Seq[SaltyWord] = Seq(SaltyWord("1", "a", "b"), SaltyWord("2", "c", "d"))
      when(dbRefMock.addValueEventListener(any())).thenAnswer( _ => valuesToSnapshot(wordSet))

      subjectRef ! GetRandomWord
      expectMsgPF(max = 1 second)(f = {
        case FoundWord(word) => assert(wordSet.contains(word))
      })
    }

    "respond with all words" in {
      val wordSet: Seq[SaltyWord] = Seq(SaltyWord("1", "a", "b"), SaltyWord("2", "c", "d"))
      when(dbRefMock.addValueEventListener(any())).thenAnswer( _ => valuesToSnapshot(wordSet))
    }

    "respond with word that matches provided id" in {
      val expectedWord = SaltyWord("1", "a", "b")
      val wordSet: Seq[SaltyWord] = Seq(expectedWord, SaltyWord("2", "c", "d"))
      when(dbRefMock.addValueEventListener(any())).thenAnswer( _ => valuesToSnapshot(wordSet))
    }
  }

  private def valuesToSnapshot[T](values: Seq[T])(implicit t: ClassTag[T]): DataSnapshot = {
    val snapshot = Mockito.mock(classOf[DataSnapshot])
    val children = values
      .map { value =>
        val childSnapshot = Mockito.mock(classOf[DataSnapshot])
        when(childSnapshot.getValue(t.runtimeClass.asInstanceOf[Class[T]])).thenReturn(value)
        childSnapshot
      }
      .asJava

    when(snapshot.getChildren).thenReturn(children)
    snapshot
  }
}
