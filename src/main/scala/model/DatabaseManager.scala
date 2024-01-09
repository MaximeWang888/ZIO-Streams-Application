package model

import java.sql.{Connection, DriverManager}

class DatabaseManager {
  private val driver = "com.mysql.cj.jdbc.Driver"
  private val url = "jdbc:mysql://localhost:3306/mydatabase"
  private val username = "root"
  private val password = "my-secret-pw"

  var connection: Connection = _

  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
  } catch {
    case e: Exception => e.printStackTrace()
  }

  def getConnection: Connection = connection

  def closeConnection(): Unit = {
    if (connection != null && !connection.isClosed) {
      connection.close()
    }
  }
}
