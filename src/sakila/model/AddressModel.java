package sakila.model;

import java.sql.*;

public class AddressModel {
    public record Address(String addr1, String addr2,
        String district, String postalCode,
        String phone,
        String city, String country) {}

    public record City(int id, String name, int country) {

        @Override
        public String toString() {
            return name;
        }
    }
    public record Country(int id, String name) {
        @Override
        public String toString() {
            return name;
        }
    }

    PreparedStatement addCountry;
    PreparedStatement addCity;
    PreparedStatement addAddress;

    PreparedStatement getCities;
    PreparedStatement getCountries;

    static AddressModel instance = null;
    public static AddressModel getInstance() throws SQLException {
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

        getCities = connection.prepareStatement("SELECT city_id, city, ci.country_id as country_id FROM city ci INNER JOIN country co ON ci.country_id = co.country_id WHERE country like ?");
        getCountries = connection.prepareStatement("SELECT country_id, country FROM country");
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
    public City[] getCities(String country) throws SQLException {
        getCities.setString(1, country == null ? "%" : country);
        ResultSet res = getCities.executeQuery();
        res.last();
        City[] cities = new City[res.getRow()];
        res.beforeFirst();
        for(int i = 0; res.next(); i++) {
            cities[i] = new City(res.getInt("city_id"), res.getString("city"), res.getInt("country_id"));
        }

        return cities;
    }
    public Country[] getCountries() throws SQLException {
        ResultSet res = getCountries.executeQuery();
        res.last();
        Country[] countries = new Country[res.getRow()];
        res.beforeFirst();
        for(int i = 0; res.next(); i++) {
            countries[i] = new Country(res.getInt("country_id"), res.getString("country"));
        }

        return countries;
    }
}
