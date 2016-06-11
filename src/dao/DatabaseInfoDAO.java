package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Casa
 * @version 10/06/2016
 */
public class DatabaseInfoDAO {
    private static final String SELECT = "SELECT version FROM DATABASE_INFO";
    
    public static int getVersion(Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            ResultSet result = statement.executeQuery();
            if( result.next() ) {
                return result.getInt("version");
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
}
