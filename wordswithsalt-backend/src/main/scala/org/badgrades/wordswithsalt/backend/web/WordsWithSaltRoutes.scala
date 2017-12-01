package org.badgrades.wordswithsalt.backend.web

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor.WriteWord
import org.badgrades.wordswithsalt.backend.domain.SaltyWord

import scala.language.postfixOps

trait WordsWithSaltRoutes { this: StrictLogging =>
  implicit val actorSystem: ActorSystem
  implicit val actorMaterializer: ActorMaterializer

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
            actorSystem.actorOf(SaltyWordDataActor.props) ! WriteWord(SaltyWord(phrase, description))
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
