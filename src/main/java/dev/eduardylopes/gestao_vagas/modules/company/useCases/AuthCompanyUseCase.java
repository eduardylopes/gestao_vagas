package dev.eduardylopes.gestao_vagas.modules.company.useCases;

import java.time.Duration;
import java.time.Instant;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import dev.eduardylopes.gestao_vagas.modules.company.dtos.AuthCompanyRequestDTO;
import dev.eduardylopes.gestao_vagas.modules.company.dtos.AuthCompanyResponseDTO;
import dev.eduardylopes.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {

  @Value("${security.token.secret}")
  private String secretKey;

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  public AuthCompanyResponseDTO execute(AuthCompanyRequestDTO authCompanyDTO) throws AuthenticationException {
    var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(() -> {
      throw new UsernameNotFoundException("Incorrect username or password");
    });

    var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException();
    }

    var expiresIn = Instant.now().plus(Duration.ofHours(2));

    var token = JWT.create().withIssuer("eduardylopes")
        .withSubject(company.getId().toString())
        .withExpiresAt(expiresIn)
        .sign(Algorithm.HMAC256(secretKey));

    return AuthCompanyResponseDTO.builder().access_token(token).expires_in(expiresIn.toEpochMilli()).build();
  }
}
