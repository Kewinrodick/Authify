package my.project.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private int jwtExpireIn;

    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails);

    }

    private String createToken(UserDetails userDetails) {
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getAuthorities());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpireIn * 1000L))
                .signWith(getSecretKey(),SignatureAlgorithm.HS512)
                .compact();

    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = secretKey.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("////////////////////");
        return key;
    }
    public Claims extractAllClaims(String token) {

            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();
            return claims;
    }
    public <T>T extractClaims(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
         return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

