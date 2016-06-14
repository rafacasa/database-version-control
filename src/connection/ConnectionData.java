package connection;

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

    public String getDatabaseName() {
        return databaseName;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
