package org.badgrades.wordswithsalt.backend.web

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

trait WordsWithSaltRoutes {
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
        parameters(('id.as[Long]?, 'phrase.as[String], 'description.as[String])) { (id, phrase, description) =>
          post {
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
