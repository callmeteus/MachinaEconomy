package org.MachinaEconomy.ThePrometeus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn;
    
    private void createTable() throws SQLException {
        conn.createStatement().executeUpdate(
            "CREATE TABLE IF NOT EXISTS `machina_economy` (" +
            "`uuid` binary(16) NOT NULL," +
            "`name` varchar(15) NOT NULL," +
            "`amount` float UNSIGNED NOT NULL," +
            "`createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "`updatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "PRIMARY KEY (`uuid`,`name`)," +
            "UNIQUE KEY `uuid` (`uuid`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
    }
    
    /**
     * Connect to the database
     * @param url Database URI
     * @param username Database username
     * @param password Database password
     * @return boolean
     */
    public boolean connect(String url, String username, String password) {
        try {
            conn        = DriverManager.getConnection(url, username, password);
            createTable();
        } catch(SQLException e) {
            return false;
        }
  
        return true;
    }
    
    /**
     * Return the database connection
     * @return Connection
     */
    public Connection getConnection() {
        return conn;
    }
}
