package sakila.model;

import java.sql.*;
import java.time.Instant;

public class CustomerModel {
    Connection connection;

    public record Customer(int id, String firstName, String lastName, String email, int address) {}

    PreparedStatement all;
    PreparedStatement removeCustomer;
    PreparedStatement addCustomer;
    PreparedStatement setCustomer;
    PreparedStatement nextID;

    ResultSet currentSet = null;

    public CustomerModel() throws SQLException {
        connection = Database.getConnection();
        all = connection.prepareStatement("""
            SELECT *
            FROM customer_list
            WHERE notes LIKE "%active%"
        """);
        removeCustomer = connection.prepareStatement("""
            UPDATE customer
            SET active=FALSE, last_update=?
            WHERE customer_id=?
        """);
        addCustomer = connection.prepareStatement("""
            INSERT INTO customer
            VALUES (?,?,?,?,?,?,TRUE,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
        """);
        setCustomer = connection.prepareStatement("""
            UPDATE customer SET
                first_name=?,
                last_name=?,
                email=?,
                address_id=?,
                last_update=CURRENT_TIMESTAMP()
            WHERE customer_id=?
        """);
        nextID = connection.prepareStatement("SELECT MAX(customer_id)+1 as id FROM customer");
    }
    private int newCustomerID() throws SQLException {
        ResultSet set = nextID.executeQuery();
        if(set.next()) return set.getInt(1);
        return -1;
    }

    public void removeUser(int clientID) throws SQLException {
        removeCustomer.setTimestamp(1, Timestamp.from(Instant.now()));
        removeCustomer.setInt(2, clientID);
        removeCustomer.executeUpdate();
    }
    public int addUser(String fName, String lName, String email, int address, int store) throws SQLException {
        int id = newCustomerID();
        addCustomer.setInt(1, id);
        addCustomer.setInt(2, store);
        addCustomer.setString(3, fName);
        addCustomer.setString(4, lName);
        addCustomer.setString(5, email);
        addCustomer.setInt(6, address);
        addCustomer.executeUpdate();
        return id;
    }
    public void updateCustomer(Customer customer) throws SQLException {
        setCustomer.setString(1, customer.firstName);
        setCustomer.setString(2, customer.lastName);
        setCustomer.setString(3, customer.email);
        setCustomer.setInt(4, customer.address);
        setCustomer.setInt(5, customer.id);
        setCustomer.executeUpdate();
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
