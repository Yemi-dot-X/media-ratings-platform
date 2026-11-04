package at.fhtw.mrp.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Legt die Tabellen an, falls sie noch nicht existieren (schema.sql). */
public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void createTables() {
        try (InputStream in = DatabaseInitializer.class
                .getResourceAsStream("/schema.sql")) {

            if (in == null) {
                throw new IllegalStateException("schema.sql not found");
            }

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(
                    "Could not initialise database. Is the container swen-db running on port 5432?", e);
        }
    }
}
