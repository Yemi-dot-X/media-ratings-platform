package at.fhtw.mrp.service;

import at.fhtw.mrp.model.User;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void testValidToken() throws NoSuchAlgorithmException {

        // Arrange: Testdaten vorbereiten
        JwtService jwtService = new JwtService();

        User user = new User();
        user.setId(1);

        // Act: JWT erstellen
        String token = jwtService.generateToken(user);

        // Assert: Ergebnisse überprüfen
        assertTrue(jwtService.validateToken(token));

        assertEquals(
                1,
                jwtService.getUserIdFromToken(token)
        );
    }
}