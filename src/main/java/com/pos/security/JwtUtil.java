package com.pos.security;

import com.pos.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "CHANGE_THIS_TO_LONG_RANDOM_SECRET_KEY_256_BITS";

    public String generateToken(User user) {

        String subject = user.getEmail() != null ? user.getEmail() : user.getMobile();

        var builder = Jwts.builder()
                .setSubject(subject)
                .claim("uid", user.getId())
                .claim("rid", user.getRole().getId())
                .claim("role", user.getRole().getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000));

        if (user.getOutletId() != null) {
            builder.claim("oid", user.getOutletId());
        }

        return builder.signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256).compact();
    }


    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
