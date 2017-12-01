package org.badgrades.wordswithsalt.backend.util

import com.google.api.core._

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
}
