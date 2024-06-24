//my practice, please ignore

import akka.actor.{Actor, ActorSystem, Props}

case class Class1(int: Int)
case class Class2(str: String)

class Greeter extends Actor {
  def receive: Receive = {
    case Class1(integer) => println("Helllo ji integer value " + integer)
    case Class2(str) => println("Helllo ji string value " + str)
    case t => println("Hello ji " + t)
  }
}

object HelloAkka extends App{
  private val system = ActorSystem("Hello-Akka")

  private val greeter = system.actorOf(Props[Greeter](), "greeter")

  greeter ! "akka"
  greeter ! Class1(15)
  greeter ! Class2("Aniket")

}
