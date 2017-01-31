package it.stanislas.scala.frp.week5.linkcrowler

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import scala.concurrent.duration._

object Controller {
  case class Check(url: String, depth: Int)
  case class Result(links: Set[String])
  case class Timeout()
}

class Controller extends Actor with ActorLogging {
  import Controller._
  import context.dispatcher

  var cache = Set.empty[String]
  var children = Set.empty[ActorRef]

  context.system.scheduler.scheduleOnce(10.seconds, self, Timeout)

  def receive = {
    case Check(url, depth) =>
      log.debug("{} checking {}", depth, url)
      if (!cache(url) && depth > 0)
        children += context.actorOf(Props(new Getter(url, depth - 1)))
      cache += url
    case Getter.Done =>
      children -= sender
      if (children.isEmpty)
        context.parent ! Result(cache)
    case Timeout => children foreach (_ ! Getter.Abort)
  }
}
