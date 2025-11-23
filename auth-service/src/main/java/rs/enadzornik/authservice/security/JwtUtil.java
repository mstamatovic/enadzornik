package rs.enadzornik.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.enadzornik.authservice.entity.Korisnik;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationMs;

    public JwtUtil(@Value("${jwt.secret:enadzornikSuperSecureSecretKey1234567890!@#}") String secret,
                   @Value("${jwt.expiration:86400000}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = expiration;
    }

    public String generateToken(Korisnik korisnik) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("korisnikId", korisnik.getKorisnikId());
        claims.put("role", korisnik.getUlogaKorisnika().name());
        claims.put("ime", korisnik.getImeKorisnika());
        claims.put("prezime", korisnik.getPrezimeKorisnika());
        return createToken(claims, korisnik.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }
}