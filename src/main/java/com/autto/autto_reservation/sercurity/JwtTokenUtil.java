package com.autto.autto_reservation.sercurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String getLoginId(String token) {
        return extractClaims(token, secretKey).get("sub").toString();
    }

    // 발급된 토큰이 만료 시간이 지났는지 체크
    public boolean isExpired(String token) {
        try {
            Date expiredDate = extractClaims(token, secretKey).getExpiration();
            return expiredDate.before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 토큰이 만료되면 true를 반환
        }
    }

    // SecretKey를 사용해 Token Parsing
    private Claims extractClaims(String token, String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}