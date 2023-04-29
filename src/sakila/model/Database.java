package sakila.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if(connection == null) {
            DatabaseConfig config = DatabaseConfig.load();
            connection = DriverManager.getConnection(config.getConnectionString(), config.getUsername(), config.getPassword());
        }
        return connection;
    }
}
