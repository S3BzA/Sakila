package sakila.model;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class FilmModel {

    private final PreparedStatement filmInsert;
    private final PreparedStatement filmSelect;
    private final PreparedStatement langInsert;
    private static Map<String, String> inputMap = new HashMap<>();
    public FilmModel() throws SQLException//Initialises the prepared statements as well as a parameter map
    {
        Connection connection = Database.getConnection();
        this.filmInsert = connection.prepareStatement("""
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
        this.filmSelect = connection.prepareStatement("""
                SELECT * FROM film
                """);
        this.langInsert = connection.prepareStatement("""
                INSERT INTO language (name)
                VALUES (?)
                """, Statement.RETURN_GENERATED_KEYS);

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

    public int insertLang(String lang_name) throws SQLException
    {
        this.langInsert.setString(1, lang_name);
        this.langInsert.executeUpdate();
        return getInsertKey(langInsert);
    }

    public void insertFilm(String lang1, String lang2) throws SQLException
    {
        System.out.println("InsertFilm");
        this.filmInsert.setString(1,inputMap.get("title"));
        this.filmInsert.setString(2,inputMap.get("description"));
        this.filmInsert.setInt(3, Integer.parseInt(inputMap.get("release_year")));
        this.filmInsert.setInt(4, insertLang(lang1));
        System.out.println("Language 1");
        this.filmInsert.setInt(5, insertLang(lang2));
        System.out.println("Language 2");
        this.filmInsert.setInt(6, Integer.parseInt(inputMap.get("rental_duration")));
        this.filmInsert.setDouble(7, Double.parseDouble(inputMap.get("rental_rate")));
        this.filmInsert.setInt(8, Integer.parseInt(inputMap.get("length")));
        this.filmInsert.setDouble(9, Double.parseDouble(inputMap.get("replacement_cost")));
        this.filmInsert.setString(10, inputMap.get("rating"));
        this.filmInsert.setString(11, inputMap.get("special_features"));
        System.out.println("InsertFilm Executed");
        this.filmInsert.executeUpdate();
        System.out.println(getInsertKey(this.langInsert));
    }

    public ResultSet getResults() throws SQLException
    {
        return filmSelect.executeQuery();
    }
}
