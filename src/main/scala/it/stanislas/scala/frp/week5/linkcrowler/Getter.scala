package it.stanislas.scala.frp.week5.linkcrowler

import akka.actor.{Actor, Status}
import akka.pattern.pipe
import org.jsoup.Jsoup

import scala.collection.JavaConverters._

object Getter {
  case object Done
  case object Abort
}

class Getter(url: String, depth: Int) extends Actor {
  import Getter._

  implicit val exec = context.dispatcher

  WebClient get url pipeTo self

  def receive = {
    case body: String =>
      for (link <- findLinks(body))
        context.parent ! Controller.Check(link, depth)
      stop()
    case _: Status.Failure => stop()
    case Abort             => stop()
  }

  def stop(): Unit = {
    context.parent ! Done
    context.stop(self)
  }

  def findLinks(body: String): Iterator[String] = {
    val document = Jsoup.parse(body, url)
    val links = document.select("a[href]")
    for {
      l <- links.iterator().asScala
    } yield l.absUrl("href")
  }

}
