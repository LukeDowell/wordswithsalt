package org.badgrades.wordswithsalt.backend.web

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor.{FoundWord, GetWordById, WriteWord}
import org.badgrades.wordswithsalt.backend.domain.SaltyWord

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
            val saltyWord = (saltyWordDataActor ? GetWordById(id)).mapTo[FoundWord]
            onSuccess(saltyWord) { foundWord =>
              complete(foundWord.saltyWord)
            }
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
