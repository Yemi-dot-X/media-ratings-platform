package at.fhtw.mrp.database;

import java.sql.Connection;
import java.sql.SQLException;


public class DatabaseConnectionTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println("Database connected!");

            System.out.println(
                    "Connection valid: " +
                            connection.isValid(2)
            );

        } catch (SQLException e) {
            System.err.println(
                    "Database connection failed: " +
                            e.getMessage()
            );
        }
    }

}
