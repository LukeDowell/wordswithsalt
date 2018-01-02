package org.badgrades.wordswithsalt.backend.actor.weather

import java.time.LocalDateTime

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated, Timers}
import akka.pattern.pipe
import org.badgrades.wordswithsalt.backend.actor.weather.WeatherDataPersistenceActor.WriteWeatherData
import org.badgrades.wordswithsalt.backend.config.Constants
import org.badgrades.wordswithsalt.backend.domain.RawWeatherData
import org.badgrades.wordswithsalt.backend.service.impl.ChiNoaaDocServiceImpl
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class WeatherDataScrapingActor extends Actor with ActorLogging with Timers {
  import WeatherDataScrapingActor._

  implicit val ec: ExecutionContext = context.dispatcher
  var persistenceActor: ActorRef = _

  override def preStart(): Unit = {
    log.info(s"Starting weather scraper")
    persistenceActor = createPersistenceActor
    context watch persistenceActor
    timers.startPeriodicTimer(ScrapeKey, StartScrape, 10 seconds)
  }

  override def receive: Receive = {
    case StartScrape =>
      log.info(s"Starting weather scraping task at ${LocalDateTime.now()}")
      Future { Jsoup.connect(Constants.WeatherDataUrl).get() } pipeTo self

    case doc: Document =>
      log.info(s"Received document with title ${doc.title()}")
      val parsedWeatherData: RawWeatherData = ChiNoaaDocServiceImpl.parse(doc)
      log.info(s"Parsed $parsedWeatherData from document with title ${doc.title()}")
      persistenceActor ! WriteWeatherData(parsedWeatherData)

    case Terminated(a) =>
      log.warning(s"$PersistenceActorName died, replacing...")
      persistenceActor = createPersistenceActor
      context watch persistenceActor
  }

  private[weather] def createPersistenceActor: ActorRef = context.actorOf(WeatherDataPersistenceActor.props, PersistenceActorName)
}

object WeatherDataScrapingActor {
  val Name = "weather-data-scraping-actor"
  def props: Props = Props[WeatherDataScrapingActor]
  val PersistenceActorName = "weather-data-persistence-actor"
  private case object ScrapeKey
  case object StartScrape
}