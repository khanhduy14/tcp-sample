package sample.tcp

import akka.actor.{ActorRef, ActorSystem}
import akka.util.ByteString
import com.google.inject.{Guice, Module}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import sample.tcp.handler.repository.ItemRepository
import sample.tcp.module.MainModule
import sample.tcp.server.TcpServer

import java.net.InetSocketAddress

object Main {
  val AllModules: Seq[Module] = Seq(MainModule)
  private val injector = Guice.createInjector(AllModules: _*)

  def main(args: Array[String]): Unit = {
    val repository = injector.instance[ItemRepository]
    val host = "0.0.0.0"
    val port = 9900
    println(s"Server started! listening to ${host}:${port}")

    val serverProps = TcpServer.props(new InetSocketAddress(host, port), repository)
    val actorSystem: ActorSystem = ActorSystem.create("MyActorSystem")
    val serverActor: ActorRef = actorSystem.actorOf(serverProps)
    serverActor ! ByteString("Starting server...")
  }
}