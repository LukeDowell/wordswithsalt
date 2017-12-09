package org.badgrades.wordswithsalt.backend.actor.weather

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}

class WeatherDataActor extends Actor with ActorLogging {
  import WeatherDataActor._

  var weatherScraperActor: ActorRef = _

  override def preStart(): Unit = {
    weatherScraperActor = createWeatherScraperActor
    context watch weatherScraperActor
  }

  override def receive: Receive = {
    case Terminated(a) =>
      log.warning(s"$WeatherScraperActorName died, replacing...")
      weatherScraperActor = createWeatherScraperActor
      context watch weatherScraperActor
  }

  private[weather] def createWeatherScraperActor = context.actorOf(WeatherDataScrapingActor.props.withDispatcher(WeatherDispatcherId), WeatherScraperActorName)
}

object WeatherDataActor {
  def props: Props = Props[WeatherDataActor]
  val WeatherDispatcherId = "weather-dispatcher"
  val WeatherScraperActorName = "weather-scraper-actor"
}
