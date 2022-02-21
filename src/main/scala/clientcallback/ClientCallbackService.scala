package clientcallback

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest, StatusCode}
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.util.{Failure, Success}

case class ATHttpClientResponse(
  status: StatusCode,
  data: String
)

object ClientCallbackService extends App {

  println("Starting Client Callback Service")

  implicit val system = ActorSystem("ClientCallbackActorSystem")

  private lazy val http = Http(system)

  def sendHttpRequest(req: HttpRequest): Future[ATHttpClientResponse] = for {
    response <- http.singleRequest(req)
    data     <- Unmarshal(response.entity).to[String]
  } yield ATHttpClientResponse(
    status = response.status,
    data   = data
  )

  val req = "https://diorsite.000webhostapp.com/tigotest.php"
//  val req = "http://10.181.96.37:8080"
  val stuff = sendHttpRequest(HttpRequest(
    method = HttpMethods.POST,
    uri    = req,
    entity = FormData(Map(
      "text"        -> req
    )).toEntity
  ))

  stuff onComplete {
    case Success(value) => println(value)
    case Failure(ex)    => println("Failure: " + ex)
  }

}
