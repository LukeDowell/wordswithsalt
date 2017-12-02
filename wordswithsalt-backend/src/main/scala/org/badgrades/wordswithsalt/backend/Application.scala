package org.badgrades.wordswithsalt.backend

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.actor.SaltyWordDataActor
import org.badgrades.wordswithsalt.backend.web.WordsWithSaltRoutes

import scala.concurrent.ExecutionContext

object Application extends WordsWithSaltRoutes with StrictLogging {

  implicit val actorSystem: ActorSystem = ActorSystem("wordsWithSaltSystem")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val saltyWordDataActor: ActorRef = actorSystem.actorOf(SaltyWordDataActor.props, SaltyWordDataActor.Name)

  def main(args: Array[String]): Unit = {
    val bindingFuture = Http().bindAndHandle(
      routes,
      Constants.Path,
      Constants.Port
    )
    logger.info(s"${FirebaseConfig.app.getName} Firebase app started")
    logger.info(s"Server online at http://${Constants.Path}:${Constants.Port}")
    bindingFuture.failed.foreach { ex => logger.error(ex.getMessage) }
  }
}
