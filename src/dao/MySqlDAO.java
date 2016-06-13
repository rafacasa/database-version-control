package dao;

import connection.ConnectionData;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DvsException;

/**
 *
 * @author Rafael Casa
 * @version 10/06/2016
 */
public class MySqlDAO implements DAO {

    private String select;
    private ConnectionData connectionData;
    private static final Logger LOGGER = LogManager.getLogger(dao.MySqlDAO.class);

    public MySqlDAO(ConnectionData connectionData) {
        this.connectionData = connectionData;
        this.select = "SELECT version FROM DATABASE_INFO";
    }

    public MySqlDAO(String databaseName, String ip, String port, String user, String password) {
        this.connectionData = new ConnectionData(databaseName, ip, port, user, password);
        this.select = "SELECT version FROM DATABASE_INFO";
    }

    @Override
    public int getVersion() throws DvsException {
        LOGGER.debug("enter in getVerion()");
        Connection connection = this.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(this.select);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("version");
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            LOGGER.debug("Exception in SELECT: " + ex);
            LOGGER.debug("Table doesn't exist: " + ex.getMessage().contains("doesn't exist"));
            if (ex.getMessage().contains("doesn't exist")) {
                return 0;
            } else {
                LOGGER.error("Exception catched at line 47" + ex);
                throw new DvsException(ex);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                LOGGER.error("Exception catched at line 59: " + ex);
                throw new DvsException(ex);
            }
        }
    }

    @Override
    public void executeSQLFile(Path fileToExecute) throws DvsException {
        LOGGER.debug("enter in executeSQLFile()");
        Connection conn = null;
        String delimiter = ";";
        Scanner scanner;
        try {
            scanner = new Scanner(fileToExecute).useDelimiter(delimiter);
        } catch (IOException ex) {
            LOGGER.error("Exception catched at line 76: " + ex);
            throw new DvsException(ex);
        }
        Statement currentStatement;
        while (scanner.hasNext()) {
            conn = this.getConnection();
            String rawStatement = scanner.next() + delimiter;
            LOGGER.debug("RAW STATEMENT: " + rawStatement);
            try {
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            } catch (SQLException ex) {
                LOGGER.error("Exception catched at line 89: " + ex);
                throw new DvsException(ex);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        LOGGER.error("Exception catched at line 96: " + ex);
                        throw new DvsException(ex);
                    }
                }
                currentStatement = null;
            }
        }
    }

    @Override
    public String getStringJBDC() {
        return "jdbc:mysql://" + this.connectionData.getIp() + ":" + this.connectionData.getPort() + "/" + this.connectionData.getDatabaseName();
    }

    @Override
    public Connection getConnection() throws DvsException {
        try {
            return DriverManager.getConnection(this.getStringJBDC(), this.connectionData.getUser(), this.connectionData.getPassword());
        } catch (SQLException ex) {
            LOGGER.error("Exception catched at line 80: " + ex);
            throw new DvsException(ex);
        }
    }
}
