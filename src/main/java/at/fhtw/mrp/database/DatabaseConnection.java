package at.fhtw.mrp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String HOST = env("DB_HOST", "localhost");
    private static final String PORT = env("DB_PORT", "5432");
    private static final String NAME = env("DB_NAME", "postgres");
    private static final String USER = env("DB_USER", "postgres");
    private static final String PASSWORD = env("DB_PASSWORD", "mysecretpassword");

    private static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORT + "/" + NAME;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
