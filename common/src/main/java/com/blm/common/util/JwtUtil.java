package com.blm.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private String secret = "blm-takeout-secret-key-for-jwt-token-generation-and-validation-2024";
    private Duration accessTokenExpiration = Duration.ofHours(2);
    private Duration refreshTokenExpiration = Duration.ofDays(7);

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成访问Token
     */
    public String generateAccessToken(String userId) {
        return generateToken(userId, accessTokenExpiration, "access");
    }

    /**
     * 生成刷新Token
     */
    public String generateRefreshToken(String userId) {
        return generateToken(userId, refreshTokenExpiration, "refresh");
    }

    /**
     * 生成Token
     */
    private String generateToken(String userId, Duration expiration, String type) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration.toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", type);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从Token中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("userId", String.class) : null;
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !isTokenExpired(claims);
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查Token是否为访问Token
     */
    public boolean isAccessToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null && "access".equals(claims.get("type", String.class));
    }

    /**
     * 检查Token是否为刷新Token
     */
    public boolean isRefreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null && "refresh".equals(claims.get("type", String.class));
    }

    /**
     * 从Token中获取Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.warn("Failed to parse token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查Token是否过期
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 获取Token剩余有效时间（毫秒）
     */
    public long getTokenRemainingTime(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            return expiration.getTime() - System.currentTimeMillis();
        }
        return 0;
    }

    // Getters and Setters for configuration properties
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAccessTokenExpiration(Duration accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public void setRefreshTokenExpiration(Duration refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
