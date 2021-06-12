package com.guidian.teaching.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 此类是Jwt常用操作工具类
 * @author dhxstart
 * @date 2021/6/12 17:05
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "guidian.jwt")
public class JwtUtils {
    /** 密钥 */
    private String secret;
    /** 超时时限 */
    private long expire;
    /** 头信息 */
    private String header;

    /**
     * @Description 生成JWT Token
     * @author dhxstart
     * @date 2021/6/12 17:05
     * @param username 用户名称
     * @return java.lang.String
     */
    public String generateToken(String username) {
        // 当前时间
        Date currentDate = new Date();
        // 过期时间
        Date expiredDate = new Date(currentDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * @Description 校验token
     * @author dhxstart
     * @date 2021/6/12 17:05
     * @param token token
     * @return io.jsonwebtoken.Claims
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * @Description 验证Token是否过期
     * @author dhxstart
     * @date 2021/6/12 17:05
     * @param claims 到期的Claims
     * @return boolean 返回true表示过期，反之亦然
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
