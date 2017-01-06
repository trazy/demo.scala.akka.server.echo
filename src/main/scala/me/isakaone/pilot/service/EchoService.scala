package me.isakaone.pilot.service

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}

object EchoService {
  def props(endpoint: InetSocketAddress): Props = Props(new EchoService(endpoint))
}

class EchoService(endpoint: InetSocketAddress) extends Actor with ActorLogging {
  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, endpoint)

  override def receive: Receive = {
    case Tcp.Connected(remote, _) =>
      log.debug(s"Remote address ${remote} connected")
      sender ! Tcp.Register(context.actorOf(EchoConnectionHandler.props(remote, sender)))
  }
}