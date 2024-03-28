package dev.eduardylopes.gestao_vagas.providers;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTCandidateProvider {

  @Value("${security.token.secret.candidate}")
  private String secretKey;

  public DecodedJWT validateToken(String token) {
    token = token.replace("Bearer ", "");

    try {
      var subject = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
      return subject;
    } catch (JWTVerificationException e) {
      return null;
    }
  }

  public String generateToken(String subject, Instant expiresIn) {

    return JWT.create()
        .withIssuer("eduardylopes")
        .withSubject(subject)
        .withClaim("roles", Arrays.asList("CANDIDATE"))
        .withExpiresAt(expiresIn)
        .sign(Algorithm.HMAC256(secretKey));
  }
}
