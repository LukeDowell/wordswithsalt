package org.badgrades.wordswithsalt.backend.util

import com.google.api.core._
import com.google.firebase.database.{DataSnapshot, DatabaseError, ValueEventListener}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.implicitConversions

object FirebaseExtensions {
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


  class ValueHandler(dc: DataSnapshot => Unit, c: DatabaseError => Unit) extends ValueEventListener {
    def onCancelled(error: DatabaseError): Unit = c(error)
    def onDataChange(snapshot: DataSnapshot): Unit = dc(snapshot)
  }
  implicit def functionToValueHandler(t: (DataSnapshot => Unit, DatabaseError => Unit)): ValueHandler =
    new ValueHandler(t._1, t._2)
}
