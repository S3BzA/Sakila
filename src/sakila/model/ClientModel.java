package sakila.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientModel {
    Connection connection;
    PreparedStatement all;
    public ClientModel() throws SQLException {
        connection = Database.getConnection();
        all = connection.prepareStatement("SELECT * FROM customer");
    }
    public ResultSet getAll() throws SQLException {
        return all.executeQuery();
    }
}
