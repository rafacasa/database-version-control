package utils;

/**
 *
 * @author Rafael Casa
 * @version 10/06/2016
 */
public class DvsException extends Exception {

    private static final long serialVersionUID = 1L;

    public DvsException() {
        super();
    }

    public DvsException(String message) {
        super(message);
    }

    public DvsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DvsException(Throwable cause) {
        super(cause);
    }
}
