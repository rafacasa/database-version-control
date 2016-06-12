package dao;

import connection.ConnectionData;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private Logger logger;

    public MySqlDAO(ConnectionData connectionData) {
        this.connectionData = connectionData;
        this.select = "SELECT version FROM DATABASE_INFO";
        this.logger = LogManager.getLogger(dao.MySqlDAO.class);
        this.logger.debug("objeto construído");
    }

    public MySqlDAO(String databaseName, String ip, String port, String user, String password) {
        this.connectionData = new ConnectionData(databaseName, ip, port, user, password);
        this.select = "SELECT version FROM DATABASE_INFO";
        this.logger = LogManager.getLogger(dao.MySqlDAO.class);
        this.logger.debug("objeto construído");
    }

    @Override
    public int getVersion() throws DvsException {
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
            this.logger.debug(ex);
            this.logger.debug(ex.getMessage().contains("doesn't exist"));
            if (ex.getMessage().contains("doesn't exist")) {
                return 0;
            } else {
                this.logger.error(ex);
                throw new DvsException(ex);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new DvsException(ex);
            }
        }
    }

    @Override
    public void executeSQLFile(Path fileToExecute) throws DvsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            this.logger.error(ex);
            throw new DvsException(ex);
        }
    }
}
