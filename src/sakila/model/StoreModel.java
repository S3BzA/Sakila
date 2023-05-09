package sakila.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreModel {
    public record Store(int id, String displayName) {
        @Override
        public String toString() {
            return displayName;
        }
    }

    PreparedStatement displayNames;
    public StoreModel() throws SQLException {
        Connection connection = Database.getConnection();
        displayNames = connection.prepareStatement("""
            SELECT DISTINCT
                store_id,
                CONCAT(
                    COALESCE(A.address, ""), " ",
                    COALESCE(A.address2, ""), ", ",
                    COALESCE(A.district, ""), " ",
                    COALESCE(C.city, "")
                )as display_name
            FROM store S
                INNER JOIN address A ON S.address_id = A.address_id
                INNER JOIN city C on A.city_id = C.city_id
                INNER JOIN city Co on C.country_id = C.country_id;
        """);
    }
    public Store[] getDisplayNames() throws SQLException {
        ResultSet set = displayNames.executeQuery();
        set.last();
        Store[] stores = new Store[set.getRow()];
        set.beforeFirst();
        int i = 0;
        while(set.next()) {
            stores[i++] = new Store(set.getInt("store_id"), set.getString("display_name"));
        }
        return stores;
    }
}
