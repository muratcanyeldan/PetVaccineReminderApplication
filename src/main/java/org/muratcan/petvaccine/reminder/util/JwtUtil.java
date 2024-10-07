package org.muratcan.petvaccine.reminder.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.config.JwtConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token";
    private final JwtConfig jwtConfig;

    public String getPreferredUsername(HttpServletRequest request) {
        return extractPreferredUsername(getJwtFromRequest(request));
    }

    public String getUserId(HttpServletRequest request) {
        return extractUserId(getJwtFromRequest(request));
    }

    public String getUserId(String token) {
        return extractUserId(token);
    }

    public String getEmail(HttpServletRequest request) {
        return extractEmail(getJwtFromRequest(request));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return extractJwtFromHeader(authorizationHeader);
    }

    private String extractEmail(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_JWT_TOKEN);
        }
    }

    private String extractUserId(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("sub", String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_JWT_TOKEN);
        }
    }

    private String extractPreferredUsername(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("preferred_username", String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_JWT_TOKEN);
        }
    }

    private Claims getClaimsFromToken(String token) {
        try {
            PublicKey publicKey = getPublicKey(jwtConfig.getSecretKey());
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_JWT_TOKEN);
        }
    }

    private PublicKey getPublicKey(String base64PublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private String extractJwtFromHeader(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is missing or invalid");
        }
    }
}

