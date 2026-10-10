package com.example.skillgap.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Service component responsible for issuing, parsing, and validating JSON Web Tokens (JWT).
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    /**
     * Resolves the cryptographic HMAC SHA signing key from the configured secret.
     *
     * @return SecretKey instance for signing and verification
     */
    private SecretKey getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception ex) {
            System.err.println("JwtTokenProvider: Error generating signing key from secret: " + ex.getMessage());
            logger.error("Failed to decode JWT signing key", ex);
            // Fallback to UTF-8 bytes if base64 decoding fails
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
    }

    /**
     * Generates a signed JWT Bearer token for the authenticated user email.
     *
     * @param email the user principal's email address
     * @return compact URL-safe JWT string
     */
    public String generateToken(String email) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            return Jwts.builder()
                    .subject(email)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception ex) {
            System.err.println("JwtTokenProvider: Error generating token for " + email + ": " + ex.getMessage());
            logger.error("Token generation failure for email: {}", email, ex);
            throw new RuntimeException("Could not generate JWT token", ex);
        }
    }

    /**
     * Extracts the subject email username from an existing JWT token.
     *
     * @param token the compact JWT string
     * @return email subject stored in the token
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception ex) {
            System.err.println("JwtTokenProvider: Error extracting email from token: " + ex.getMessage());
            logger.error("Failed to extract subject from token", ex);
            return null;
        }
    }

    /**
     * Validates whether a given token signature and expiration date are intact.
     *
     * @param token the compact JWT string
     * @return true if valid and active, false if expired or malformed
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            System.err.println("JwtTokenProvider: Invalid JWT signature/format: " + ex.getMessage());
            logger.warn("Invalid JWT token signature or structure: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.err.println("JwtTokenProvider: Expired JWT token: " + ex.getMessage());
            logger.warn("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.err.println("JwtTokenProvider: Unsupported JWT token: " + ex.getMessage());
            logger.warn("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("JwtTokenProvider: JWT claims string is empty: " + ex.getMessage());
            logger.warn("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            System.err.println("JwtTokenProvider: Unexpected validation exception: " + ex.getMessage());
            logger.error("Unexpected JWT validation failure", ex);
        }
        return false;
    }
}
