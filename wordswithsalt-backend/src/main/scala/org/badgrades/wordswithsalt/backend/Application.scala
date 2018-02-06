package org.badgrades.wordswithsalt.backend

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import org.badgrades.wordswithsalt.backend.actor.cluster.ClusterMonitorActor
import org.badgrades.wordswithsalt.backend.actor.weather.WeatherDataScrapingActor
import org.badgrades.wordswithsalt.backend.actor.word.SaltyWordActor
import org.badgrades.wordswithsalt.backend.config.Constants
import org.badgrades.wordswithsalt.backend.web.WordsWithSaltRoutes

import scala.concurrent.ExecutionContext

object Application extends WordsWithSaltRoutes with StrictLogging {
  implicit val actorSystem: ActorSystem = ActorSystem("wordsWithSaltSystem")
  implicit val ec: ExecutionContext = actorSystem.dispatchers.lookup(Constants.RoutesDispatcher)
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val saltyWordDataActor: ActorRef = actorSystem.actorOf(SaltyWordActor.props, SaltyWordActor.Name)

  def main(args: Array[String]): Unit = {
    // Setup and bind web server
    val bindingFuture = Http().bindAndHandle(routes, Constants.Path, Constants.Port)
    logger.info(s"Server online at http://${Constants.Path}:${Constants.Port}")
    bindingFuture.failed.foreach { ex => logger.error(ex.getMessage) }

    // Initialize and schedule the weather scraping system
    actorSystem.actorOf(
      WeatherDataScrapingActor.props.withDispatcher(Constants.WeatherDispatcher),
      WeatherDataScrapingActor.Name
    )

    logger.info("Creating ClusterMonitorActor...")
    actorSystem.actorOf(ClusterMonitorActor.props)
  }
}
