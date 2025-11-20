package com.shenzhewei.quiz.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * JWT 工具类
 */
public class JwtUtil {

    // 生成密钥（实际项目中要保存在配置中）
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("mySuperSecretKey1234567890abcdef".getBytes());

    // Token 有效期：1小时
    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    /**
     * 生成 JWT 字符串（带自定义 Claims）
     */
    public static String generateTokenWithClaims(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成 JWT 字符串（只包含用户名）
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT 并返回 Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析 JWT 并获取用户名
     */
    public static String parseTokenGetUsername(String token) {
        return parseToken(token).getSubject();
    }
}
