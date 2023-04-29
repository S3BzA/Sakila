package sakila.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffModel {
    private final PreparedStatement selectAll;

    public StaffModel() throws SQLException {
        Connection connection = Database.getConnection();
        selectAll = connection.prepareStatement("""
            SELECT
                first_name,
                last_name,
                address,
                address2,
                district,
                city,
                postal_code,
                phone,
                store_id as store,
                IF(active, "Active", "Inactive") as active
            FROM staff s
                INNER JOIN address a ON s.address_id = a.address_id
                INNER JOIN city ci ON a.city_id = ci.city_id
                INNER JOIN country co ON co.country_id = ci.country_id;
        """);
    }


    public ResultSet getAll() throws SQLException {
        return selectAll.executeQuery();
    }
}
