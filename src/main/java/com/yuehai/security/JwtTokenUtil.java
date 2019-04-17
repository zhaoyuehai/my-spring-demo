package com.yuehai.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * Created by zhaoyuehai 2019/4/10
 */
@Component
public class JwtTokenUtil {
    /**
     * 密钥
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 过期时间（秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成令牌
     *
     * @param claims     数据声明
     * @param expiration 过期时间 -毫秒
     * @return 令牌
     */
    public String generateToken(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 生成过期时间
     *
     * @return 过期时间 -毫秒
     */
    public long generateExpiration() {
        return System.currentTimeMillis() + expiration * 1000;

    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成令牌
     *
     * @param userName userName
     * @return 令牌
     */
    public String generateToken(String userName, long expiration) {
        HashMap<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userName);
        claims.put("created", new Date());
        return generateToken(claims, expiration);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 刷新令牌
     *
     * @param token 旧的令牌
     * @return 新的令牌
     */
    public String refreshToken(String token, long expiration) {
        Claims claims = getClaimsFromToken(token);
        claims.put("created", new Date());
        return generateToken(claims, expiration);
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
