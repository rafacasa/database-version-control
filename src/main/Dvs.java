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

    private static final Logger logger = LogManager.getLogger(main.Dvs.class);
    private final Path SqlFolder;
    private final DAO dao;

    public Dvs(Path SqlFolder, DAO dao) {
        this.SqlFolder = SqlFolder;
        this.dao = dao;
    }

    public void verifyVersion() throws DvsException {
        logger.debug("entrou em verifyVersion()");
        int version = this.dao.getVersion();
        int lastVersion = this.getLastVersion();
        if(version < lastVersion) {
            logger.debug("database needs to be updated");
        } else if (version > lastVersion) {
            throw new DvsException("Database version is newer than the newer file");
        } else {
            logger.debug("database is up to date");
        }
    }

    private int getLastVersion() throws DvsException {
        logger.debug("entrou no metodo getLastVersion()");
        try {
            return Files.list(this.SqlFolder)
                    .filter(p -> p.toString().matches("[\\s\\S]*\\\\\\d+.sql"))
                    .max(Comparator.comparingInt(p -> this.getVersionFromFilename(p.toString())))
                    .map(p -> this.getVersionFromFilename(p.toString()))
                    .orElseThrow(DvsException::new);
        } catch (IOException ex) {
            logger.catching(ex);
            logger.debug(ex);
            throw new DvsException(ex);
        }
    }

    private int getVersionFromFilename(String path) {
        String[] array = path.split("\\\\");
        logger.debug(array);
        String filename = array[array.length - 1];
        logger.debug(filename);
        String target = filename.split("\\.")[0];
        logger.debug(target);
        return Integer.parseInt(target);
    }
}
