package net.plancktong.voting.database.credentials;

import lombok.Getter;

@Getter
public class SQLCredentials {
	
    private final String hostName;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;

    /**
     * @param hostName
     * @param port
     * @param databaseName
     * @param username
     * @param password
     */
    public SQLCredentials(String hostName, int port, String databaseName, String username, String password) {
        this.hostName = hostName;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }
    
    /**
     * @param hostName
     * @param databaseName
     * @param username
     * @param password
     */
    public SQLCredentials(String hostName, String databaseName, String username, String password) {
        this.hostName = hostName;
        this.port = 3306;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

}
