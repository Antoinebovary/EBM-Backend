package com.rra.ebm.EBMApplication.service;

import com.rra.ebm.EBMApplication.domain.Users;
import com.rra.ebm.EBMApplication.repository.UsersRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    private UsersRepo usersRepo;

    public Date getPasswordChangeDate(Users users) {
        return users != null ? users.getChangedAt() : null;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        Users users = usersRepo.findById(Integer.parseInt(username)).orElse(null);
        if (users == null) {
            return false;
        }
        Date passwordChangeDate = getPasswordChangeDate(users);
        return passwordChangeDate == null || !passwordChangeDate.after(extractExpiration(token));
    }

    public String generateToken(String username, String roles, boolean isDefault) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        if (isDefault) {
            claims.put("isDefault", true);
        }
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60*1000)) // 2 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
