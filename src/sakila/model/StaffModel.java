package sakila.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffModel {
    private final PreparedStatement selectAll;

    private String searchName = null;
    private Boolean searchActive = null;

    public StaffModel() throws SQLException {
        Connection connection = Database.getConnection();
        selectAll = connection.prepareStatement("""
            SELECT
                CONCAT(first_name, ' ', last_name) as name,
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
                INNER JOIN country co ON co.country_id = ci.country_id
            WHERE
                (first_name LIKE ? OR last_name LIKE ?) AND
                (active LIKE ?)
        """);
    }

    public void setSearchName(String searchAddress) {
        this.searchName = searchAddress;
    }
    public void setSearchActive(Boolean searchActive) {
        this.searchActive = searchActive;
    }

    public ResultSet getAll() throws SQLException {
        String name = searchName == null ? "%" : "%" + searchName + "%";
        String active = searchActive == null ? "%" : "%" + (searchActive ? 1 : 0) + "%";
        selectAll.setString(1, name);
        selectAll.setString(2, name);
        selectAll.setString(3, active);
        System.out.println(active);

        return selectAll.executeQuery();
    }
}
