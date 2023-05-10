package sakila.model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class FilmModel {

    private final PreparedStatement filmInsert;
    private final PreparedStatement filmSelect;
    private final PreparedStatement addLanguage;
    private static final Map<String, String> inputMap = new HashMap<>();
    public FilmModel() throws SQLException//Initialises the prepared statements as well as a parameter map
    {
        Connection connection = Database.getConnection();
        filmInsert = connection.prepareStatement("""
                INSERT INTO film (
                title,
                description,
                release_year,
                language_id,
                original_language_id,
                rental_duration,
                rental_rate,
                length,
                replacement_cost,
                rating,
                special_features
                )
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """);//leaves out last_update and film_id (auto-incremented)
        filmSelect = connection.prepareStatement(" SELECT * FROM film");
        addLanguage = connection.prepareStatement(
            "INSERT INTO language(name) VALUES (?)",
            Statement.RETURN_GENERATED_KEYS
        );

//        inputMap.put("film_id",null);
        inputMap.put("title",null);
        inputMap.put("description",null);
        inputMap.put("release_year",null);
        inputMap.put("language_id",null);
        inputMap.put("original_language_id",null);
        inputMap.put("rental_duration",null);
        inputMap.put("rental_rate",null);
        inputMap.put("length",null);
        inputMap.put("replacement_cost",null);
        inputMap.put("rating",null);
        inputMap.put("special_features",null);
//        inputMap.put("last_update",null);
    }
    public int addLanguage(String language) throws SQLException {
        addLanguage.setString(1, language);
        addLanguage.executeUpdate();
        return getInsertKey(addLanguage);
    }

    private static int getInsertKey(PreparedStatement stmt) throws SQLException
    {
        ResultSet set = stmt.getGeneratedKeys();
        if(set.next()) return set.getInt(1);
        throw new SQLException();

    }
    public void setParameter(String key,String value)
    {
        inputMap.replace(key,value);
    }

    public String getParameter(String key)
    {
        return inputMap.get(key);
    }

    public void insertFilm(String lang, String orgLang) throws SQLException
    {
        int id1 = addLanguage(lang);
        if(orgLang != null) filmInsert.setInt(5, addLanguage(orgLang));
        else filmInsert.setNull(5, Types.INTEGER);

        filmInsert.setString(1,inputMap.get("title"));
        filmInsert.setString(2,inputMap.get("description"));
        filmInsert.setInt(3, Integer.parseInt(inputMap.get("release_year")));
        filmInsert.setInt(4, id1);
        filmInsert.setInt(6, Integer.parseInt(inputMap.get("rental_duration")));
        filmInsert.setDouble(7, Double.parseDouble(inputMap.get("rental_rate")));
        filmInsert.setInt(8, Integer.parseInt(inputMap.get("length")));
        filmInsert.setDouble(9, Double.parseDouble(inputMap.get("replacement_cost")));
        filmInsert.setString(10, inputMap.get("rating"));
        filmInsert.setString(11, inputMap.get("special_features"));
        filmInsert.executeUpdate();

    }

    public ResultSet getResults() throws SQLException
    {
        return filmSelect.executeQuery();
    }
}
