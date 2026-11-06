package at.fhtw.mrp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Baut die Verbindung zur PostgreSQL Datenbank auf.
 * Die Standardwerte passen zu docker-compose.yml.
 * Mit den Umgebungsvariablen DB_HOST, DB_PORT, DB_NAME, DB_USER und DB_PASSWORD änderst du sie.
 */
public enum DatabaseManager {
    INSTANCE;

    public Connection getConnection() {
        String url = "jdbc:postgresql://"
                + env("DB_HOST", "localhost") + ":"
                + env("DB_PORT", "5432") + "/"
                + env("DB_NAME", "postgres");
        try {
            return DriverManager.getConnection(
                    url,
                    env("DB_USER", "postgres"),
                    env("DB_PASSWORD", "mysecretpassword"));
        } catch (SQLException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
