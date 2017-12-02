package org.badgrades.wordswithsalt.backend.web

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor.{FoundWord, GetRandomWord, GetWordById, WriteWord}

import scala.concurrent.duration._
import scala.language.postfixOps

trait WordsWithSaltRoutes extends JsonSupport { this: StrictLogging =>
  implicit val actorSystem: ActorSystem
  implicit val actorMaterializer: ActorMaterializer
  implicit val timeout: Timeout = 3 seconds
  implicit val saltyWordDataActor: ActorRef

  val routes: Route =
    logRequestResult("wordsWithSalt", akka.event.Logging.InfoLevel) {
      path("word") {
        parameters('id.as[String]) { id =>
          get {
            val wordResultFuture = (saltyWordDataActor ? GetWordById(id)).mapTo[FoundWord]
            onSuccess(wordResultFuture) { wordResult =>
              complete(wordResult.saltyWord)
            }
          }
        } ~
        parameters(('id.as[String]?, 'phrase.as[String], 'description.as[String])) { (id, phrase, description) =>
          post {
            saltyWordDataActor ! WriteWord(phrase, description)
            complete("add word")
          } ~
          put {
            complete("update word")
          }
        } ~
        get {
          val wordResultFuture = (saltyWordDataActor ? GetRandomWord).mapTo[FoundWord]
          onSuccess(wordResultFuture) { wordResult =>
            complete(wordResult.saltyWord)
          }
        }
      } ~
      pathSingleSlash {
        get {
          complete("get page")
        }
      }
    }

  implicit def exceptionHandler: ExceptionHandler = ExceptionHandler {
    case unknown =>
      logger.error(s"Unknown exception! Message=${unknown.getMessage}", unknown)
      complete(HttpResponse(
        StatusCodes.InternalServerError,
        entity = "The server has encountered an unexpected error. Please try again later."
      ))
  }
}
