package com.example.notice.auth;

import com.example.notice.config.ConfigurationService;
import com.example.notice.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthProvider {

    private final ConfigurationService configurationService;

    public static final String BEARER = "Bearer";

    private final Long SECOND = 1000L;
    private final Long MINUTE = 60 * SECOND;
    private final Long HOUR = 60 * MINUTE;
    private final Long AUTH_DURATION = 255 * HOUR;

    private final String SUBJECT = "board";


    @Override
    public String createAuthentication(Member member) {
        SecretKey key = getSecretKey();
        Date date = getExpriationDate(AUTH_DURATION);

        return Jwts.builder()
                .setSubject(SUBJECT)
                .claim("memberId", member.getMemberId())
                .claim("name", member.getName())
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(date)
                .compact();
    }

    @Override
    public Member verify(String bearerToken) {
        String jwtToken = bearerToken.replaceFirst(BEARER, "").strip();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        long memberId = (int) claims.get("memberId");
        String name = (String) claims.get("name");
        return Member.builder()
                .memberId(memberId)
                .name(name)
                .build();
    }

    private Date getExpriationDate(long expirationTime) {
        return new Date(new Date().getTime() + expirationTime);
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(configurationService.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
