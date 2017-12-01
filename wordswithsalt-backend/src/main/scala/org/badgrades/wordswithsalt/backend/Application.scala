package org.badgrades.wordswithsalt.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.web.WordsWithSaltRoutes

import scala.concurrent.ExecutionContext

object Application extends WordsWithSaltRoutes with StrictLogging {

  implicit val actorSystem: ActorSystem = ActorSystem("wordsWithSaltSystem")
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val bindingFuture = Http().bindAndHandle(
      routes,
      Constants.Path,
      Constants.Port
    )
    println(s"Firebase started with name=${FirebaseConfig.app.getName}")
    println(s"Server online at http://${Constants.Path}:${Constants.Port}/\nPress RETURN to stop...")
    bindingFuture.failed.foreach { ex => logger.error(ex.getMessage) }
  }
}
