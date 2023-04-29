package sakila.model;

public class DatabaseConfig {
    private final String proto, host, port, database;
    private final String username, password;

    public static DatabaseConfig load() throws IllegalStateException {
        return new DatabaseConfig();
    }

    private DatabaseConfig() throws IllegalStateException {
        proto = requireVariable("dvdrental_DB_PROTO");
        host = requireVariable("dvdrental_DB_HOST");
        database = requireVariable("dvdrental_DB_NAME");
        port = System.getenv("dvdrental_DB_PORT");

        username = requireVariable("dvdrental_DB_USERNAME");
        password = requireVariable("dvdrental_DB_PASSWORD");
    }

    private static String requireVariable(String name) throws IllegalStateException {
        String value = System.getenv(name);
        if(value == null) throw new IllegalStateException("Missing environment variable: \"" + name + "\"");
        return value;
    }


    public String getConnectionString() {
        String connection = proto + "://" + host;
        if(port != null) connection += ":" + port;
        connection += "/" + database;
        return connection;
    }

    public String getPassword() { return password; }
    public String getUsername() { return username; }
}
