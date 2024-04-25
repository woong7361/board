package com.example.notice.auth.provider;

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

    public static final String MEMBER_ID = "memberId";
    public static final String MEMBER_NAME = "name";
    private static final String SUBJECT = "board";
    public static final String BEARER = "Bearer";

    private final ConfigurationService configurationService;

    @Override
    public String createAuthentication(Member member) {
        SecretKey key = getSecretKey();
        Date date = getExpriationDate(configurationService.getJwtDuration() * 1000);

        return Jwts.builder()
                .setSubject(SUBJECT)
                .claim(MEMBER_ID, member.getMemberId())
                .claim(MEMBER_NAME, member.getName())
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

        long memberId = (int) claims.get(MEMBER_ID);
        String name = (String) claims.get(MEMBER_NAME);
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
