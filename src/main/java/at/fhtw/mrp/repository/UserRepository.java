package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.User;

import java.util.List;

public interface UserRepository {

    User findById(int id);
    User findByUsername(String username);
    void save(User user);
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findAll();
}
