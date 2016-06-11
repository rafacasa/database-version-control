package main;

import connection.ConnectionData;
import dao.DatabaseInfoDAO;

/**
 *
 * @author Rafael Casa
 * @version 10/06/2016
 */
public class Dvs {
    ConnectionData data;
    public void verifyVersion() {
        DatabaseInfoDAO.getVersion(data.getConnection());
    }
}
