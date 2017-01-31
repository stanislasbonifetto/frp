package it.stanislas.scala.frp.week5

import akka.actor.{Actor, Props}

/**
  * Created by stanislas on 30/01/2017.
  */
class CounterMain extends Actor {
  val counter = context.actorOf(Props[Counter], "counter")

  counter ! "incr"
  counter ! "incr"
  // counter ! "incr"
  counter ! "get"

  override def receive = {
    case count:Int => println(s"count was $count")
      context.stop(self)
  }
}
