package sakila.model;

import java.sql.*;

public class AddressModel {
    public record Address(String addr1, String addr2,
        String district, String postalCode,
        String phone,
        String city, String country) {}

    PreparedStatement addCountry;
    PreparedStatement addCity;
    PreparedStatement addAddress;

    static AddressModel instance = null;
    static AddressModel getInstance() throws SQLException {
        if(instance == null) {
            instance = new AddressModel();
        }
        return instance;
    }

    private AddressModel() throws SQLException {
        Connection connection = Database.getConnection();

        addAddress = connection.prepareStatement("INSERT INTO country(country) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        addCity = connection.prepareStatement("INSERT INTO city(country_id, country) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        addCountry = connection.prepareStatement("INSERT INTO address(address, address2, district, city_id, postal_code, phone) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    }
    private static int getInsertKey(PreparedStatement stmt) throws SQLException {
        ResultSet set = stmt.getGeneratedKeys();
        if(set.next()) return set.getInt(1);
        return -1;
    }
    public int addAddress(Address address) throws SQLException {
        addCountry.setString(1, address.country);
        addCountry.executeUpdate();

        addCity.setInt(1, getInsertKey(addCountry));
        addCity.setString(2, address.city);
        addCity.executeUpdate();

        addAddress.setString(3, address.addr1);
        addAddress.setString(4, address.addr2);
        addAddress.setString(5, address.district);
        addAddress.setInt(6, getInsertKey(addCity));
        addAddress.setString(7, address.postalCode);
        addAddress.setString(8, address.phone);
        addAddress.executeUpdate();
        return getInsertKey(addAddress);
    }
}
