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

import dev.eduardylopes.gestao_vagas.modules.company.dtos.AuthCompanyDTO;
import dev.eduardylopes.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {

  @Value("${security.token.secret}")
  private String secretKey;

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  public String execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
    var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(() -> {
      throw new UsernameNotFoundException("Incorrect username or password");
    });

    var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException();
    }

    var token = JWT.create().withIssuer("eduardylopes")
        .withSubject(company.getId().toString())
        .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
        .sign(Algorithm.HMAC256(secretKey));

    return token;
  }
}
