package model

import java.sql.{Connection, DriverManager}

// Class responsible for managing the database connection
class DatabaseManager {
  // Database connection details
  private val driver = "com.mysql.cj.jdbc.Driver"
  private val url = "jdbc:mysql://localhost:3306/mydatabase"
  private val username = "root"
  private val password = "my-secret-pw"

  // Variable to store the database connection
  var connection: Connection = _

  // Initialize the database connection in the constructor
  try {
    // Load the JDBC driver
    Class.forName(driver)
    // Establish the database connection
    connection = DriverManager.getConnection(url, username, password)
  } catch {
    case e: Exception => e.printStackTrace() // Print stack trace if an exception occurs during connection setup
  }

  // Method to retrieve the database connection
  def getConnection: Connection = connection

  // Method to close the database connection
  def closeConnection(): Unit = {
    if (connection != null && !connection.isClosed) {
      connection.close()
    }
  }
}
