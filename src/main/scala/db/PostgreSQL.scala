package db

import java.sql.{ Connection, DriverManager, ResultSet }

object PostgreSQL extends App {

  println("Postgres connector")

  classOf[org.postgresql.Driver]
  val username = "postgres"
  val password = "Earthly@2011"
  val con_st = "jdbc:postgresql://localhost:5432/testdb"
  val conn = DriverManager.getConnection(con_st, username, password)
  try {
    val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

    val rs = stm.executeQuery("SELECT * FROM accounts")

    while(rs.next) {
      println(rs.getString("name"))
    }
  } finally {
    conn.close()
  }

}
