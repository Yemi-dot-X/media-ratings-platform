package at.fhtw.mrp.service;

import at.fhtw.mrp.model.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;


public class JwtService {
//erzeugt eineheren zufallsSchlüssel
    private final SecretKey secretKey;

    public JwtService(){
        String encodedKey = System.getenv("JWT_SECRET");

        if(encodedKey == null || encodedKey.isBlank()){
            throw new IllegalArgumentException("JWT_SECRET is missing");
        }

        byte[] keyBytes = Decoders.BASE64.decode(encodedKey);

        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT key is too short");
        }

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
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
