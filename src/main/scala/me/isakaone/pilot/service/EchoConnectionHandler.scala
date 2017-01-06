package me.isakaone.pilot.service

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.io.Tcp

object EchoConnectionHandler {
  def props(remote: InetSocketAddress, connection: ActorRef): Props = Props(new EchoConnectionHandler(remote, connection))
}

class EchoConnectionHandler(remote: InetSocketAddress, connection: ActorRef) extends Actor with ActorLogging {
  context.watch(connection)

  def receive: Receive = {
    case Tcp.Received(data) =>
      val text = data.utf8String.trim
      log.debug(s"Received '${text}' from remote address ${remote}")
      text match {
        case "close" => context.stop(self)
        case _       => sender ! Tcp.Write(data)
      }
    case _: Tcp.ConnectionClosed =>
      log.debug(s"Stopping, because connection for remote address ${remote} closed")
      context.stop(self)
    case Terminated(`connection`) =>
      log.debug(s"Stopping, because connection for remote address ${remote} died")
      context.stop(self)
  }
}
