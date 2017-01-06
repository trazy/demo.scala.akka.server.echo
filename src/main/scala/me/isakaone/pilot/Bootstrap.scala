package me.isakaone.pilot

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import me.isakaone.pilot.service.EchoService

object Bootstrap extends App {
  def start {
    var lineSperator = System.getProperty("line.separator")
    val system = ActorSystem("echo-server")
    val endpoint = new InetSocketAddress(1234)
    system.actorOf(EchoService.props(endpoint), "echo-server")
    scala.io.StdIn.readLine(s"Hit ENTER to exit ...${lineSperator}")
    system.terminate()
  }
  start
}
