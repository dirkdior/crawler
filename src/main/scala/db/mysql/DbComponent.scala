package db.mysql

import slick.jdbc.{ JdbcProfile, MySQLProfile }

private[mysql] trait DbComponentT {

  val driver: JdbcProfile

  import driver.api._

  val db: Database
}

private[mysql] trait MysqlDbComponentT extends DbComponentT {

  val driver = MySQLProfile
  import driver.api.Database

  val db: Database = MysqlDbConnection.connectionPool
}

private object MysqlDbConnection {
  import MySQLProfile.api.Database
  val connectionPool = Database.forConfig("db.mysql")
}
