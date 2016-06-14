package main;

import dao.DAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DvsException;

/**
 * Main class of the library
 *
 * @author Rafael Casa
 * @version 13/06/2016
 */
public class Dvs {

    private static final Logger LOGGER = LogManager.getLogger(main.Dvs.class);
    private final Path SqlFolder;
    private final DAO dao;

    /**
     * Makes a instance od Dvs, the heart of this library
     *
     * @param SqlFolder A {@link java.nio.file.Path} to the folder where will be
     * the .sql's files and the database.info file.
     * @param dao A instance of {@link dao.DAO} with tha connection data to the
     * database. Each SGDB has a implementation of {@link dao.DAO}.
     */
    public Dvs(Path SqlFolder, DAO dao) {
        this.SqlFolder = SqlFolder;
        this.dao = dao;
    }

    /**
     * The main method of this library. This method verify the database_info on
     * the server and the database.info at the file. If both have the same
     * version nothing is done. If the file has a newer version than the server,
     * so the files from the missing versions will be executed on the server.
     *
     * @throws DvsException If the server's version is newer than the file's
     * one.
     */
    public void verifyVersion() throws DvsException {
        LOGGER.debug("enter in verifyVersion()");
        int version = this.dao.getVersion();
        int lastVersion = this.getLastVersion();
        if (version < lastVersion) {
            LOGGER.debug("database needs to be updated");
            for (int i = version + 1; i <= lastVersion; i++) {
                LOGGER.debug("database will be updated to version: " + i);
                this.updateDatabase(i);
            }
        } else if (version > lastVersion) {
            LOGGER.error("database version: " + version + ".sql newer file: " + lastVersion);
            throw new DvsException("Database version is newer than the newer file");
        } else {
            LOGGER.debug("database is up to date");
        }
    }

    /**
     * Gets the version on the file database.info.
     *
     * @return The version on the file database.info
     * @throws DvsException If an {@link java.io.IOException} is catched or if
     * the file database.info hasn't a single integer number.
     */
    private int getLastVersion() throws DvsException {
        LOGGER.debug("enter in getLastVersion()");
        String filename = "database.info";
        Path databaseInfo = Paths.get(this.SqlFolder.toString() + "\\" + filename);
        LOGGER.debug("SqlFolder.toString() + database.info: " + databaseInfo.toString());
        try {
            int[] array = Files.lines(databaseInfo).mapToInt(s -> Integer.parseInt(s)).toArray();
            LOGGER.debug("Array Length: " + array.length);
            if (array.length != 1) {
                LOGGER.error("The file database.info must have a single number (version) of the most recent .sql file.\nThe array size is: " + array.length);
                throw new DvsException("The file database.info must have a single number (version) of the most recent .sql file.");
            }
            return array[0];
        } catch (IOException ex) {
            LOGGER.error("Exception catched at line 84: " + ex);
            throw new DvsException(ex);
        }
    }

    /**
     * With a version number gets the {@link java.nio.file.Path} to the matching
     * .sql file.
     *
     * @param version The wanted version.
     * @return The {@link java.nio.file.Path} to the matching .sql file.
     */
    private Path getFilenameFromVersion(int version) {
        LOGGER.debug("enter in getFilenameFromVersion()");
        String filename = version + ".sql";
        LOGGER.debug("SqlFolder.toString() + filename: " + this.SqlFolder.toString() + "\\" + filename);
        return Paths.get(this.SqlFolder.toString() + "\\" + filename);
    }

    /**
     * Updates the server to a determined version.
     *
     * @param version the version to update the server.
     * @throws DvsException Throwed at {@link dao.DAO}
     * @see dao.DAO
     */
    private void updateDatabase(int version) throws DvsException {
        Path fileToUpdateServer = this.getFilenameFromVersion(version);
        this.dao.executeSQLFile(fileToUpdateServer);
    }
}
