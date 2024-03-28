package dev.eduardylopes.gestao_vagas.modules.company.useCases;

import java.time.Duration;
import java.time.Instant;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.eduardylopes.gestao_vagas.modules.company.dtos.AuthCompanyRequestDTO;
import dev.eduardylopes.gestao_vagas.modules.company.dtos.AuthCompanyResponseDTO;
import dev.eduardylopes.gestao_vagas.modules.company.repositories.CompanyRepository;
import dev.eduardylopes.gestao_vagas.providers.JWTCompanyProvider;

@Service
public class AuthCompanyUseCase {

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  JWTCompanyProvider jwtCompanyProvider;

  public AuthCompanyResponseDTO execute(AuthCompanyRequestDTO authCompanyDTO) throws AuthenticationException {
    var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(() -> {
      throw new UsernameNotFoundException("Incorrect username or password");
    });

    var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException();
    }

    var expiresIn = Instant.now().plus(Duration.ofHours(2));
    var token = this.jwtCompanyProvider.generateToken(company.getId().toString(), expiresIn);

    return AuthCompanyResponseDTO.builder().access_token(token).expires_in(expiresIn.toEpochMilli()).build();
  }
}
