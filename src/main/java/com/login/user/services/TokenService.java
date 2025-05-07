package com.login.user.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String jwtSecret;

    @Value("${api.security.token.issuer}")
    private String jwtIssuer;

    @Value("${api.security.token.expiration}")
    private Long jwtExpirationInSeconds;

    public String generateToken(UserDetails user){
        try{
            var roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var algorithm = Algorithm.HMAC256(jwtSecret);
            var token = JWT.create()
                .withIssuer(jwtIssuer)
                .withSubject(user.getUsername())
                .withExpiresAt(getExpirationDate())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .sign(algorithm);

            return token;
        } catch(JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token");
        }
    }

    public String validateToken(String token){
        try{
            var algorithm = Algorithm.HMAC256(jwtSecret);

            return JWT.require(algorithm)
                .withIssuer(jwtIssuer)
                .build()
                .verify(token)
                .getSubject();
        } catch(JWTVerificationException exception){
            throw new RuntimeException("Erro ao gerar token: ", exception);
        }
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(jwtExpirationInSeconds / 3600).toInstant(ZoneOffset.of("-03:00"));
    }
}
