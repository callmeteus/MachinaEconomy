package org.MachinaEconomy.ThePrometeus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private Connection conn;
    private String url;
    private String username;
    private String password;
    
    /**
     * Creates the plugin MySQL table
     * @throws SQLException 
     */
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
        this.url = url;
        this.username = username;
        this.password = password;

        return reconnect() != null;
    }
    
    /**
     * Create a connection to a MySQL server
     * @return Connection
     */
    private Connection reconnect() {
        try {
            conn = DriverManager.getConnection(url + "?autoReconnect=true&useSSL=false&characterEncoding=UTF-8", username, password);
            createTable();
            
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Returns an active database connection
     * @return Connection
     */
    public Connection getConnection() {
        try {
            if (conn.isClosed()) {
                return reconnect();
            } else {
                return conn;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            
            return reconnect();
        }
    }
}
