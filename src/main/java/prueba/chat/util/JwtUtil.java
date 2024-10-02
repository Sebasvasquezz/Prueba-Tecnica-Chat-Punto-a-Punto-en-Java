package prueba.chat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil is a utility class for handling JSON Web Tokens (JWT).
 * It provides methods for generating tokens, extracting claims (like username and expiration),
 * and validating tokens.
 */
@Component
public class JwtUtil {

    @Value("${jwt.key}")  
    private String SECRET_KEY;

    /**
     * Extracts the username from the given JWT token.
     * 
     * @param token The JWT token from which the username is to be extracted.
     * @return The username (subject) extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     * 
     * @param token The JWT token from which the expiration date is to be extracted.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the token based on the claims resolver function.
     * 
     * @param <T> The type of the claim to extract.
     * @param token The JWT token.
     * @param claimsResolver A function to resolve the desired claim from the claims object.
     * @return The claim value extracted from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * 
     * @param token The JWT token from which claims are extracted.
     * @return A Claims object containing all the claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     * 
     * @param token The JWT token to check.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a new JWT token for the specified username.
     * 
     * @param username The username for which the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with the specified claims and subject (username).
     * 
     * @param claims The claims to include in the token.
     * @param subject The subject (usually the username) for which the token is created.
     * @return The generated JWT token.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Validates the given JWT token by checking if the username matches and the token is not expired.
     * 
     * @param token The JWT token to validate.
     * @param username The username to validate against the token's subject.
     * @return True if the token is valid and not expired, false otherwise.
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}

