package org.badgrades.wordswithsalt.backend.actor.cluster

import akka.actor.{Actor, ActorLogging, Address, Props}
import akka.cluster.Cluster

class ClusterMonitorActor extends Actor with ActorLogging {
  import akka.cluster.ClusterEvent._

  var members = Set.empty[Address]

  override def preStart(): Unit = {
    Cluster(context.system).subscribe(self, InitialStateAsEvents, classOf[MemberEvent])
    log.info("Subscribed to akka cluster")
  }

  def receive: Receive = {
    case MemberJoined(member) =>
      log.info(s"Member joined: ${member.address}")
      members += member.address

    case MemberUp(member) =>
      log.info("Member up: {}", member.address)
      members += member.address

    case MemberRemoved(member, _) =>
      log.info("Member removed: {}", member.address)
      members -= member.address
  }
}

object ClusterMonitorActor {
  def props: Props = Props[ClusterMonitorActor]
}
