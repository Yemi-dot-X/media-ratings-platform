package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.User;

public interface UserRepository extends Repository<User, Integer> {
    // Returns null when the username does not exist.
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


     //updateById ändert nur name, email und dateOfBirth.
     //Username und Passwort bleiben unverändert.

    @Override
    User updateById(Integer id, User entity);
}
