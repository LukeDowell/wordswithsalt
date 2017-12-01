package org.badgrades.wordswithsalt.backend.web

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor.WriteWord
import org.badgrades.wordswithsalt.backend.domain.SaltyWord

import scala.concurrent.duration._
import scala.language.postfixOps

trait WordsWithSaltRoutes { this: StrictLogging =>
  implicit val actorSystem: ActorSystem
  implicit val actorMaterializer: ActorMaterializer
  implicit val timeout: Timeout = 1 second
  implicit val saltyWordDataActor: ActorRef

  val routes: Route =
    logRequestResult("wordsWithSalt", akka.event.Logging.InfoLevel) {
      path("word") {
        parameters('id.as[Long]) { id =>
          get {
            complete("word by id")
          }
        } ~
        parameters('phrase.as[String]) { phrase =>
          get {
            complete("word by phrase")
          }
        } ~
        parameters(('id.as[String]?, 'phrase.as[String], 'description.as[String])) { (id, phrase, description) =>
          post {
            saltyWordDataActor ! WriteWord(SaltyWord(phrase, description))
            complete("add word")
          } ~
          put {
            complete("update word")
          }
        } ~
        get {
          complete("random word")
        }
      } ~
      pathSingleSlash {
        get {
          complete("get page")
        }
      }
    }
}
