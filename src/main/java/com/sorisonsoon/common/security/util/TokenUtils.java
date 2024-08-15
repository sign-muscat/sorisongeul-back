//package com.sorisonsoon.common.security.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class TokenUtils {
//
//    private static String secret;
//
//    private static Long accessTokenExpiration;
//
//    private static Long refreshTokenExpiration;
//
//
//    private static final String BEARER = "Bearer ";
//
//
//    @Value("${jwt.secret}")
//    public void setJwtSecretKey(String secret) {
//        TokenUtils.secret = secret;
//    }
//
//    @Value("${jwt.access.expiration}")
//    public void setAccessTokenExpiration(Long accessTokenExpiration) {
//        TokenUtils.accessTokenExpiration = accessTokenExpiration;
//    }
//
//    @Value("${jwt.refresh.expiration}")
//    public void setRefreshTokenExpiration(Long refreshTokenExpiration) {
//        TokenUtils.refreshTokenExpiration = refreshTokenExpiration;
//    }
//
//    public static boolean isValidToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(createSignature()).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
//            return false;
//        }
//    }
//
////    public static String getToken(String token) {
////        return (token != null && token.startsWith(BEARER))
////                ? token.replace(BEARER, "") : null;
////    }
//    public static String getToken(String token) {
//        if (token != null && token.startsWith(BEARER)) {
//            return token.substring(BEARER.length());
//        }
//        return null;
//    }
//
//    public static String getIdFromToken(String accessToken) {
//        return Jwts.parserBuilder()
//                   .setSigningKey(createSignature())
//                   .build()
//                   .parseClaimsJws(accessToken)
//                   .getBody()
//                   .get("id").toString();
//    }
//
//    public static String createAccessToken(Map<String, Object> userInfo) {
//
//        Claims claims = Jwts.claims().setSubject("AccessToken");
//        claims.putAll(userInfo);
//
//        return Jwts.builder()
//                   .setHeader(createHeader())
//                   .setClaims(claims)
//                   .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
//                   .signWith(createSignature(), SignatureAlgorithm.HS512)
//                   .compact();
//    }
//
//    public static String createRefreshToken() {
//        return Jwts.builder()
//                   .setHeader(createHeader())
//                   .setSubject("RefreshToken")
//                   .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
//                   .signWith(createSignature(), SignatureAlgorithm.HS512)
//                   .compact();
//    }
//
//    private static Key createSignature() {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    private static Map<String, Object> createHeader() {
//        Map<String, Object> header = new HashMap<>();
//        header.put("type", "jwt");
//        header.put("date", System.currentTimeMillis());
//        return header;
//    }
//}

package com.sorisonsoon.common.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class TokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private static final String BEARER = "Bearer ";

    private Key createSignature() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String createAccessToken(UserDetails userDetails) {
        return doCreateToken(userDetails.getUsername(), accessTokenExpiration);
    }

    // Refresh Token 생성
    public String createRefreshToken() {
        return doCreateToken(null, refreshTokenExpiration);
    }

    // 실제 토큰 값 반환
    public static String getToken(String token) {
        return (token != null && token.startsWith(BEARER))
                ? token.substring(BEARER.length()) : null;
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // JWT 토큰 유효성 검증
    public Boolean isValidToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰 만료 여부 확인
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 토큰 만료 날짜 추출
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // JWT 토큰에서 클레임 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임을 추출하는 메서드
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(createSignature()).build().parseClaimsJws(token).getBody();
    }

    // 토큰 생성 메서드
    private String doCreateToken(String subject, Long expirationTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(createSignature(), SignatureAlgorithm.HS512)
                .compact();
    }

}

