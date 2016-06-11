package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DvsException;

/**
 *
 * @author Rafael Casa
 * @version 10/06/2016
 */
public class ConnectionData {
    private final String databaseName, ip, port, user, password;

    public ConnectionData(String databaseName, String ip, String port, String user, String password) {
        this.databaseName = databaseName;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    private String getJBDCString() {
        return "jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.databaseName;
    }
    
    public Connection getConnection() throws DvsException {
        try {
            return DriverManager.getConnection(this.getJBDCString(), this.user, this.password);
        } catch (SQLException ex) {
            
            throw new DvsException(ex.getSQLState());
        }
    }
}
