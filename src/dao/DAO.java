package dao;

import utils.DvsException;

/**
 * The main class {@link main.Dvs} has to receive a implementation of this
 * interface made for the SGDB you want to use with this software. The
 * constructor has to receive an {@link connection.ConnectionData}'s object with
 * the Connection Data to use in the program.
 *
 * @author Rafael Casa
 * @version 13/06/2016
 */
public interface DAO {

    /**
     * Makes the JBDC's String of the connection data from this DAO.
     *
     * @param withDatabaseSet whes set true, the JDBC's String has the database
     * name.
     * @return the JBDC's String
     */
    public String getStringJBDC(boolean withDatabaseSet);

    /**
     * Makes a {@link java.sql.Connection} with the connection data from this
     * DAO.
     *
     * @param withDatabaseSet when set true, the Connection is made from a
     * JDBC's String with Database name.
     * @return A Connection
     * @throws utils.DvsException when there is an error in the connection with
     * the database.
     */
    public java.sql.Connection getConnection(boolean withDatabaseSet) throws DvsException;

    /**
     * Gets the version of the database running on the connection data of this
     * DAO.
     *
     * @return the version on the DATABASE_INFO's table of the database. If the
     * connection is ok, but the table doesn't exist, then 0 is returned.
     * @throws utils.DvsException when there is an error in the connection with
     * the database.
     */
    public int getVersion() throws DvsException;

    /**
     * Execute the statements in the file into the connection data of this DAO.
     *
     * @param fileToExecute The {@link java.nio.file.Path} to the file to be
     * executed.
     * @throws utils.DvsException when there is an error in the connection with
     * the database.
     */
    public void executeSQLFile(java.nio.file.Path fileToExecute) throws DvsException;
}
