package db.mysql

import akka.Done
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private case class SessionDetailsRow(
//  id: Int,
  session_id: String,
  user_id: Int,
  insertion_day: Int,
  service_code: String,
  phone_number: String,
  duration_in_ms: Int,
  price: String,
  status: String,
  network_code: String,
  text: String
)

private[mysql] trait SessionDetailsMappingT {

  this: DbComponentT =>

  import driver.api._

  class SessionDetailsMapping(tag: Tag) extends Table[SessionDetailsRow](tag, "ussd_session_details") {
//    def id: Rep[Int]                     = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def session_id: Rep[String]            = column[String]("session_id", O.PrimaryKey)
    def user_id: Rep[Int]                  = column[Int]("user_id")
    def insertion_day: Rep[Int]            = column[Int]("insertion_day")
    def service_code: Rep[String]          = column[String]("service_code")
    def phone_number: Rep[String]          = column[String]("phone_number")
    def duration_in_ms: Rep[Int]           = column[Int]("duration_in_ms")
    def price: Rep[String]                 = column[String]("price")
    def status: Rep[String]                = column[String]("status")
    def network_code: Rep[String]          = column[String]("network_code")
    def text: Rep[String]                  = column[String]("text")
    def * : ProvenShape[SessionDetailsRow] = (
      session_id,
      user_id,
      insertion_day,
      service_code,
      phone_number,
      duration_in_ms,
      price,
      status,
      network_code,
      text
    ).<>(SessionDetailsRow.tupled, SessionDetailsRow.unapply)

  }

  val sessionDetailsInfo: TableQuery[SessionDetailsMapping] = TableQuery[SessionDetailsMapping]

}

private[mysql] trait SessionDetailsComponentT extends SessionDetailsMappingT {

  this: DbComponentT =>

  import driver.api._

  def fetchAll: Future[List[SessionDetailsRow]] = {
    val query = sessionDetailsInfo.to[List]
    db.run(query.result)
  }

  def create(
    d: SessionDetailsRow
  ): Future[Done] = {
    val insertQuery = sessionDetailsInfo += SessionDetailsRow(
      session_id     = d.session_id,
      user_id        = d.user_id,
      insertion_day  = d.insertion_day,
      service_code   = d.service_code,
      phone_number   = d.phone_number,
      duration_in_ms = d.duration_in_ms,
      price          = d.price,
      status         = d.status,
      network_code   = d.network_code,
      text           = d.text
    )
    db.run(insertQuery) map (_ => Done)
  }

}

object SessionDetailsMapper extends SessionDetailsComponentT with MysqlDbComponentT
