package dev.eduardylopes.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.eduardylopes.gestao_vagas.modules.candidate.dtos.AuthCandidateRequestDTO;
import dev.eduardylopes.gestao_vagas.modules.candidate.dtos.AuthCandidateResponseDTO;
import dev.eduardylopes.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import dev.eduardylopes.gestao_vagas.providers.JWTCandidateProvider;

@Service
public class AuthCandidateUseCase {

  @Autowired
  private JWTCandidateProvider jwtCandidateProvider;

  @Autowired
  private CandidateRepository candidateRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO)
      throws AuthenticationException {
    var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username()).orElseThrow(() -> {
      throw new UsernameNotFoundException("Incorrect username or password");
    });

    var passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException();
    }

    var expiresIn = Instant.now().plus(Duration.ofHours(2));
    var token = this.jwtCandidateProvider.generateToken(candidate.getId().toString(), expiresIn);

    return AuthCandidateResponseDTO.builder().access_token(token).expires_in(expiresIn.toEpochMilli()).build();

  }
}
