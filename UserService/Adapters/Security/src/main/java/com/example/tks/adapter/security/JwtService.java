package com.example.tks.adapter.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @PreAuthorize("permitAll()")
    public String createToken(UUID id, String login, String roles) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("tks-b")
                .issuedAt(now)
                .expiresAt(now.plus(jwtExpiration, ChronoUnit.HOURS))
                .subject(id.toString())
                .claim("authorities", roles)
                .claim("login", login)
                .build();

        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claims);
        return jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    public String getUserLogin(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();

//        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret_key)).build();
//        DecodedJWT decodedJWT = verifier.verify(token);
//        return decodedJWT.getSubject();
    }
}
