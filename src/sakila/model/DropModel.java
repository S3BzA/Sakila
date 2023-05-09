package sakila.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DropModel {
    String SID, country, name;

    PreparedStatement searchDrops;

    public DropModel() throws SQLException {
        Connection conn = Database.getConnection();
        searchDrops = conn.prepareStatement("""
            SELECT * FROM customer_list
            WHERE
                NOT (notes LIKE "%active%") AND
                SID LIKE ? AND
                country LIKE ? AND
                name LIKE ?
        """);

    }

    public void searchStore(String store) {
        SID = store;
    }
    public void searchName(String name) {
        this.name = name;
    }
    public void searchCountry(String country) {
        this.country = country;
    }
    private String getSearchString(String s) {
        if(s == null) return "%";
        return "%" + s + "%";
    }
    public ResultSet fetchAll() throws SQLException {
        searchDrops.setString(1, getSearchString(SID));
        searchDrops.setString(2, getSearchString(country));
        searchDrops.setString(3, getSearchString(name));
        return searchDrops.executeQuery();
    }



}
