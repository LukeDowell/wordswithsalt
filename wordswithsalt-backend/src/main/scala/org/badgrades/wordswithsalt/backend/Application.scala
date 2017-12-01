package org.badgrades.wordswithsalt.backend

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.badgrades.wordswithsalt.backend.web.WordsWithSaltRoutes

import scala.concurrent.ExecutionContext

object Application extends WordsWithSaltRoutes {

  implicit val actorSystem: ActorSystem = ActorSystem("wordsWithSaltSystem")
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  val logger = Logging(actorSystem, getClass)

  def main(args: Array[String]): Unit = {
    val path = "localhost"
    val port = 6969
    val bindingFuture = Http().bindAndHandle(routes, path, port)

    println(s"Server online at http://$path:$port/\nPress RETURN to stop...")
    bindingFuture.failed.foreach { ex => logger.error(ex.getMessage) }
  }
}
