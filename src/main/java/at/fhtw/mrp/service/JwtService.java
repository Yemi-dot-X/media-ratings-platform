package at.fhtw.mrp.service;

import at.fhtw.mrp.exception.UnauthorizedException;
import at.fhtw.mrp.model.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class JwtService {
//erzeugt eineheren zufallsSchlüssel
    private final SecretKey secretKey;

    public JwtService() {
        String password = System.getenv("JWT_SECRET");

        if (password == null || password.isBlank()) {
            // Fallback nur für lokale Tests und die Abgabe
            password = "mysecretpassword";
        }
        try{
        byte[] keyBytes = MessageDigest
                .getInstance("SHA-256")
                .digest(password.getBytes(StandardCharsets.UTF_8));



        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("SHA-256 unavailable", e);
    }
    }
    public String generateToken(User user){

        return Jwts.builder()
                //speichert die UserID
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000
                ))
                .signWith(secretKey)
                .compact();
    }

    //Liest die User-ID aus dem Authorization Header ("Bearer ...").
    //  Wirft UnauthorizedException (401), wenn der Header fehlt oder das Token ungültig ist.

    public int requireUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid token");
        }

        String token = authorizationHeader.substring(7);

        if (!validateToken(token)) {
            throw new UnauthorizedException("Invalid token");
        }

        try {
            return getUserIdFromToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public boolean validateToken(String token){
        try{
            //bereitet das Lesen vor
            //prüft die Signatur
            //prüft den Token nach Ablaufdatum
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false;

        }
    }

    public int getUserIdFromToken(String token){
        String subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Integer.parseInt(subject);
    }
}
