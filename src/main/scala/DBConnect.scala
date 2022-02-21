
import java.sql.{Connection,DriverManager}

object DBConnect extends App {

  // connect to the database named "mysql" on port 8889 of localhost
  val url = "jdbc:mysql://elarian-zeus.mysql.database.azure.com:3306/ussd_extractor"
  val driver = "com.mysql.jdbc.Driver"
  val username = "hydra_netuser@elarian-zeus"
  val password = "Hyse_e6dAb71F56c0x"
  var connection:Connection = _
  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement
    val rs = statement.executeQuery("SELECT * FROM ussd_session_details;")
//    while (rs.next) {
//      val host = rs.getString("host")
//      val user = rs.getString("user")
//      println("host = %s, user = %s".format(host,user))
//    }

    println(rs)
  } catch {
    case e: Exception => e.printStackTrace()
  }
  connection.close()

}
