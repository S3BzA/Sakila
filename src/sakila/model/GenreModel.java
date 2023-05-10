package sakila.model;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreModel {
    private final PreparedStatement selectFields;

    public GenreModel() throws SQLException
    {
        Connection connection = Database.getConnection();
        this.selectFields = connection.prepareStatement("""
                SELECT
                    i.store_id,
                    c.name,
                    COUNT(*) AS number_of_films
                FROM
                    inventory i
                    INNER JOIN film f ON i.film_id = f.film_id
                    INNER JOIN film_category fc ON f.film_id = fc.film_id
                    INNER JOIN category c ON fc.category_id = c.category_id
                GROUP BY
                    i.store_id,
                    c.name
                """);
    }

    public ResultSet fetchResults() throws SQLException
    {
        return this.selectFields.executeQuery();
    }
}
