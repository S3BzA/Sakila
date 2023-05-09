package sakila.model;

import java.sql.*;

public class CustomerModel {
    Connection connection;

    public record Customer(String firstName, String lastName, String email, int address) {}

    PreparedStatement all;
    PreparedStatement removeCustomer;
    PreparedStatement addCustomer;
    PreparedStatement setCustomer;

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
            SET active=FALSE
            WHERE customer_id=?
        """);
        addCustomer = connection.prepareStatement("""
            INSERT INTO customer(store_id, first_name, last_name, email, address_id)
            VALUES (?,?,?,?,?);
        """);
        setCustomer = connection.prepareStatement("""
            UPDATE customer SET
                first_name=?,
                last_name=?,
                email=?,
                address_id=?,
            WHERE customer_id=?
        """);
    }

    public void removeUser(int clientID) throws SQLException {
        removeCustomer.setInt(1, clientID);
        removeCustomer.executeUpdate();
    }
    public void addUser(String fName, String lName, String email, int address, int store) throws SQLException {
        addCustomer.setInt(1, store);
        addCustomer.setString(2, fName);
        addCustomer.setString(3, lName);
        addCustomer.setString(4, email);
        addCustomer.setInt(5, address);
        addCustomer.executeUpdate();
    }
    public void updateCustomer(int id, Customer customer) throws SQLException {
        setCustomer.setString(1, customer.firstName);
        setCustomer.setString(2, customer.lastName);
        setCustomer.setString(3, customer.email);
        setCustomer.setInt(4, customer.address);
        setCustomer.setInt(5, id);
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
