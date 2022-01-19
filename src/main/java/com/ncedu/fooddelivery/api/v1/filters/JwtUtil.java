package com.ncedu.fooddelivery.api.v1.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.expiration.time}")
    private long JWT_EXPIRATION_TIME;

    @Value("${jwt.secret}")
    private String SECRET;

    public final String PREFIX = "Bearer ";
    public final String HEADER = "Authorization";

    public boolean isAuthHeaderNotValid(String header) {
        return header == null || !header.startsWith(PREFIX);
    }

    public String getJwt(String header) {
        return header.replace(PREFIX,"");
    }

    public String createToken(UserDetails userDetails) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Boolean isTokenValid(String token) {
        getAllClaimsFromToken(token);
        return true; //if success parsing JWT claims
    }

    public Boolean isTokenNotExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.after(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
