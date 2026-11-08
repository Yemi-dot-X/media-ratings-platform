
package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserRepositoryTest {

    @Test
    void testSaveUser() {

        JdbcUserRepository repository =
                new JdbcUserRepository();

        User user = new User(
                0,
                "testuser",
                "Test User",
                "test@example.com",
                "test-hash-placeholder",
                LocalDate.of(2000, 1, 1)
        );

        User saveedUser = repository.create(user);

        assertTrue(user.getId() > 0);
    }
}
