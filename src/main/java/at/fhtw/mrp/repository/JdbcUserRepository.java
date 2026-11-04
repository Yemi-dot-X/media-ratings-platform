package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//excuteQuery() liest die daten mit Select
//excuteUpdate() verändert Daten mit INSERT UPDATE o DELETE
public class JdbcUserRepository implements UserRepository{
    @Override
    public User findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getDate("date_of_birth").toLocalDate()
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }

        return null;
    }



    @Override
    public User findByUsername(String username) {

        String sql =
                "SELECT * FROM users WHERE username = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getDate("date_of_birth").toLocalDate()
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }

        return null;
    }


    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            insertUser(user);
        } else {
            updateUser(user);
        }
    }

    private void updateUser(User user) {

        String sql = """
        UPDATE users
        SET username = ?,
            name = ?,
            email = ?,
            password = ?,
            date_of_birth = ?
        WHERE id = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setDate(5, Date.valueOf(user.getDateOfBirth()));
            statement.setInt(6, user.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    private void insertUser(User user) {

        String sql = """
        INSERT INTO users
        (username, name, email, password, date_of_birth)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setDate(5, Date.valueOf(user.getDateOfBirth()));

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    int generatedId = result.getInt("id");
                    user.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql =
                "SELECT * FROM users WHERE email = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)){
            statement.setString(1, email);

            try(ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql =
                "SELECT * FROM users WHERE username = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)){
            statement.setString(1, username);

            try(ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }



    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        List<User> users = new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                User user = new User(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getDate("date_of_birth").toLocalDate()
                );
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }

        return users;

    }
}
