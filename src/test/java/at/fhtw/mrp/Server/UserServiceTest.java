/*package at.fhtw.mrp.Server;

import at.fhtw.mrp.model.User;
import at.fhtw.mrp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private final UserService userService = new UserService(null, null);

    @Test
    void testPasswordHashing() {

        // Testbenutzer erstellen
        User user = new User(
                0,
                "Sophie",
                "sophie@example.com",
                "Test123456!"
        );

        // Benutzer registrieren
        userService.register(user);

        // Gespeicherten Hash holen
        String storedPassword = user.getPassword();

        // Passwort darf kein Klartext sein
        assertNotEquals(
                "Test123456!",
                storedPassword
        );

        //Richtiges Passwort überprüfen
        assertTrue(
                BCrypt.checkpw(
                        "Test123456!",
                        storedPassword
                )
        );

        //Falsches Passwort überprüfen
        assertFalse(
                BCrypt.checkpw(
                        "WrongPassword",
                        storedPassword
                )
        );
    }
}*/