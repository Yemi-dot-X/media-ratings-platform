package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.User;
public interface UserRepository {

    User findById(int id);
    User findByUsername(String username);
    void save(User user);
}
