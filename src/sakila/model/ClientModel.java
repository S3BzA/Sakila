package sakila.model;

import java.sql.*;
import java.time.Instant;

public class ClientModel {
    Connection connection;

    PreparedStatement all;
    PreparedStatement removeClient;
    PreparedStatement addClient;
    PreparedStatement nextID;

    ResultSet currentSet = null;

    public ClientModel() throws SQLException {
        connection = Database.getConnection();
        all = connection.prepareStatement("""
            SELECT *
            FROM customer_list
            WHERE notes LIKE "%active%"
        """);
        removeClient = connection.prepareStatement("""
            UPDATE customer
            SET active=FALSE, last_update=?
            WHERE customer_id=?
        """);
        addClient = connection.prepareStatement("""
            INSERT INTO customer
            VALUES (?,?,?,?,?,?,TRUE,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
        """);
        nextID = connection.prepareStatement("SELECT MAX(customer_id)+1 as id FROM customer");
    }
    private int newCustomerID() throws SQLException {
        ResultSet set = nextID.executeQuery();
        if(set.next()) return set.getInt(1);
        return -1;
    }

    public void removeUser(int clientID) throws SQLException {
        removeClient.setTimestamp(1, Timestamp.from(Instant.now()));
        removeClient.setInt(2, clientID);
        removeClient.executeUpdate();
    }
    public int addUser(String fName, String lName, String email, int address, int store) throws SQLException {
        int id = newCustomerID();
        addClient.setInt(1, id);
        addClient.setInt(2, store);
        addClient.setString(3, fName);
        addClient.setString(4, lName);
        addClient.setString(5, email);
        addClient.setInt(6, address);
        addClient.executeUpdate();
        return id;
    }
    public ResultSet getAll() throws SQLException {
        currentSet = all.executeQuery();
        return currentSet;
    }
    public int getCustomerFromRow(int rowIndex) {
        if(currentSet == null) return -1;
        try {
            currentSet.absolute(rowIndex + 1);
            return currentSet.getInt("id");
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            return -1;

        }

    }
}
