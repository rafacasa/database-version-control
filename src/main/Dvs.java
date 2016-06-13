package main;

import dao.DAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DvsException;

/**
 *
 * @author Rafael Casa
 * @version 10/06/2016
 */
public class Dvs {
    
    private static final Logger LOGGER = LogManager.getLogger(main.Dvs.class);
    private final Path SqlFolder;
    private final DAO dao;
    
    public Dvs(Path SqlFolder, DAO dao) {
        this.SqlFolder = SqlFolder;
        this.dao = dao;
    }
    
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
            throw new DvsException("Database version is newer than the newer file");
        } else {
            LOGGER.debug("database is up to date");
        }
    }
    
    private int getLastVersion() throws DvsException {
        LOGGER.debug("enter in getLastVersion()");
        try {
            return Files.list(this.SqlFolder)
                    .filter(p -> p.toString().matches("[\\s\\S]*\\\\\\d+.sql"))
                    .max(Comparator.comparingInt(p -> this.getVersionFromFilename(p.toString())))
                    .map(p -> this.getVersionFromFilename(p.toString()))
                    .orElseThrow(DvsException::new);
        } catch (IOException ex) {
            LOGGER.catching(ex);
            LOGGER.debug("Exception in getLastVersion: " + ex);
            throw new DvsException(ex);
        }
    }
    
    private int getVersionFromFilename(String path) {
        LOGGER.debug("enter in getVersionFromFilename()");
        String[] array = path.split("\\\\");
        String filename = array[array.length - 1];
        LOGGER.debug("Filename: " + filename);
        String target = filename.split("\\.")[0];
        LOGGER.debug("Retorno: " + target);
        return Integer.parseInt(target);
    }
    
    private Path getFilenameFromVersion(int version) {
        LOGGER.debug("enter in getFilenameFromVersion()");
        String filename = version + ".sql";
        
    }
    
    private void updateDatabase(int version) throws DvsException {
        Path fileToUpdateServer = this.getFilenameFromVersion(version);
        this.dao.executeSQLFile(fileToUpdateServer);
    }
}
