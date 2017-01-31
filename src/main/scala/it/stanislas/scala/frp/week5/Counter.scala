package it.stanislas.scala.frp.week5

import akka.actor.Actor

/**
  * Created by stanislas on 30/01/2017.
  */
class Counter extends Actor {
  var count = 0

  override def receive = {
    case "incr" => count += 1
    case "get" => sender ! count
  }

}
