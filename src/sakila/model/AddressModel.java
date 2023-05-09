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

    PreparedStatement getCountry;

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

        addCountry = connection.prepareStatement("INSERT INTO country(country) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        addCity = connection.prepareStatement("INSERT INTO city(country_id, country) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        addAddress = connection.prepareStatement("INSERT INTO address(address, address2, district, city_id, postal_code, phone) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        getCountry = connection.prepareStatement("SELECT country_id FROM country WHERE country=?");

        getCities = connection.prepareStatement("SELECT city_id, city, ci.country_id as country_id FROM city ci INNER JOIN country co ON ci.country_id = co.country_id WHERE country like ?");
        getCountries = connection.prepareStatement("SELECT country_id, country FROM country");
    }
    private static int getInsertKey(PreparedStatement stmt) throws SQLException {
        ResultSet set = stmt.getGeneratedKeys();
        if(set.next()) return set.getInt(1);
        return -1;
    }

    private int addCity(String city, int country) throws SQLException {
        addCity.setInt(1, country);
        addCity.setString(1, city);
        addCity.executeUpdate();
        return getInsertKey(addCity);
    }

    public int addAddress(Address address) throws SQLException {
        int country = getCountryID(address.country);
        int city = addCity(address.city, country);

        addAddress.setString(1, address.addr1);
        addAddress.setString(2, address.addr2);
        addAddress.setString(3, address.district);
        addAddress.setInt(4, city);
        addAddress.setString(5, address.postalCode);
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

    /**
     * Retrieves a list of country names and their IDs
     * @return An array of countries with their name and database ID
     * @throws SQLException Exception?
     */
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

    /**
     * Retrieves the ID of the country with the given name,
     * or inserts it if no country is found
     * @param country The country to search for
     * @return The ID of the country
     * @throws SQLException Lekke mos :)
     */
    private int getCountryID(String country) throws SQLException {
        getCountry.setString(1, country);
        ResultSet set = getCountry.executeQuery();
        if(set.next()) return set.getInt(1);

        addCountry.setString(1, country);
        addCountry.executeUpdate();

        set = addCountry.getGeneratedKeys();
        set.next();
        return set.getInt(1);
    }
}
