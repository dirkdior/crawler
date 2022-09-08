package random

object Emailing extends App {

  import courier._, Defaults._
  import scala.util._
  import javax.mail.internet.InternetAddress

  val mailer: Mailer = Mailer("smtp.gmail.com", 465)
    .auth(true)
    .as("d@d.com", "pass")
    .ssl(true)()
  mailer(
    Envelope
      .from(new InternetAddress("d@d.com"))
      .to(new InternetAddress("d@gmail.com"))
      .subject("miss you")
      .content(Text("hi mom"))
  ).onComplete {
    case Success(_)  => println("message delivered")
    case Failure(ex) => println("delivery failed: {}", ex)
  }
//
//  mailer(
//    Envelope
//      .from(new InternetAddress("you@work.com"))
//      .to(new InternetAddress("boss@work.com"))
//      .subject("tps report")
//      .content(
//        Multipart()
//          .attach(new java.io.File("tps.xls"))
//          .html("<html><body><h1>IT'S IMPORTANT</h1></body></html>")
//      )
//  )
//    .onComplete {
//      case Success(_)  => println("delivered report")
//      case Failure(ex) => println("delivery failed: {}", ex)
//    }

  Thread.sleep(100000)

}
