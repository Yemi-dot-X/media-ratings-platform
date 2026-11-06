package at.fhtw.mrp.repository;

import at.fhtw.mrp.database.DataAccessException;
import at.fhtw.mrp.database.UnitOfWork;
import at.fhtw.mrp.exception.UserAlreadyExistsException;
import at.fhtw.mrp.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements UserRepository {
    private static final String COLUMNS = "id, username, name, email, password, date_of_birth";
    @Override
    public List<User> readAll() {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement(
                     "SELECT " + COLUMNS + " FROM users ORDER BY id")) {
            List<User> users = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(map(resultSet));
                }
            }
            unitOfWork.commitTransaction();
            return users;
        } catch (SQLException e) {
            throw new DataAccessException("Lesen der Users nicht erfolgreich", e);
        }
    }

    @Override
    public User readById(Integer id) {
        return queryOne("SELECT " + COLUMNS + " FROM users WHERE id = ?", id);
    }

    @Override
    public User findByUsername(String username) {
        return queryOne("SELECT " + COLUMNS + " FROM users WHERE username = ?", username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return queryOne("SELECT " + COLUMNS + " FROM users WHERE username = ?", username) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return queryOne("SELECT " + COLUMNS + " FROM users WHERE email = ?", email) != null;
    }

    @Override
    public User create(User entity) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement("""
                     INSERT INTO users (username, name, email, password, date_of_birth)
                     VALUES (?, ?, ?, ?, ?)
                     RETURNING id, username, name, email, password, date_of_birth
                     """)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setDate(5, Date.valueOf(entity.getDateOfBirth()));
            try {
                User created;
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    created = map(resultSet);
                }
                unitOfWork.commitTransaction();
                return created;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                throw e;
            }
        } catch (SQLException e) {
            // 23505 = unique_violation (Username oder E-Mail gibt es schon)
            if ("23505".equals(e.getSQLState())) {
                throw new UserAlreadyExistsException("Username or email already exists");
            }
            throw new DataAccessException("Anlegen des Users nicht erfolgreich", e);
        }
    }

    @Override
    public User updateById(Integer id, User entity) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement("""
                     UPDATE users
                     SET name = ?,
                         email = ?,
                         date_of_birth = ?
                     WHERE id = ?
                     RETURNING id, username, name, email, password, date_of_birth
                     """)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getEmail());
            statement.setDate(3, Date.valueOf(entity.getDateOfBirth()));
            statement.setInt(4, id);
            try {
                User updated = null;
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        updated = map(resultSet);
                    }
                }
                unitOfWork.commitTransaction();
                return updated;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                throw e;
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new UserAlreadyExistsException("Email already exists");
            }
            throw new DataAccessException("Aktualisieren des Users nicht erfolgreich", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement(
                     "DELETE FROM users WHERE id = ?")) {
            statement.setInt(1, id);
            try {
                int affected = statement.executeUpdate();
                unitOfWork.commitTransaction();
                return affected > 0;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Löschen des Users nicht erfolgreich", e);
        }
    }

    //Führt eine Abfrage mit genau einem Parameter aus und liefert die erste Zeile oder null.
    private User queryOne(String sql, Object parameter) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setObject(1, parameter);
            User user = null;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = map(resultSet);
                }
            }
            unitOfWork.commitTransaction();
            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Lesen des Users nicht erfolgreich", e);
        }
    }

    private User map(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getDate("date_of_birth").toLocalDate());
    }
}



