package com.jecfalo.palermus_api.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jecfalo.palermus_api.modules.users.models.User;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    private static final long ACCESS_TOKEN_HOURS= 3;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("palermus")
                    .withSubject(user.getUsername())
                    .withExpiresAt(accessTokenExpired())
                    .withClaim("userId", user.getProfile().getId())
                    .withClaim("role", user.getProfile().getUserType().name())
                    .withClaim("type", "access")
                    .sign(algorithm);
        }catch (JWTCreationException ex){
            throw new RuntimeException("Error al generar el token", ex);
        }
    }
    public String getSecret(String jwtToken){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("palermus")
                    .build()
                    .verify(jwtToken)
                    .getSubject();
        }catch (JWTVerificationException ex){
            throw new RuntimeException("Este token puede ser invalido");
        }
    }
    private Instant accessTokenExpired(){
        return LocalDateTime.now(ZoneOffset.UTC).plusHours(ACCESS_TOKEN_HOURS).toInstant(ZoneOffset.UTC);
    }
}
