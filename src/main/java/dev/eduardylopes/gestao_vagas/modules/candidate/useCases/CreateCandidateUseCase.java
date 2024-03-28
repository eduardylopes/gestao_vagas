package dev.eduardylopes.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.eduardylopes.gestao_vagas.exceptions.UserFoundException;
import dev.eduardylopes.gestao_vagas.modules.candidate.entities.CandidateEntity;
import dev.eduardylopes.gestao_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class CreateCandidateUseCase {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  private CandidateRepository candidateRepository;

  public CandidateEntity execute(CandidateEntity candidateEntity) {
    this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail())
        .ifPresent(user -> {
          throw new UserFoundException();
        });

    var encodedPassword = passwordEncoder.encode(candidateEntity.getPassword());
    candidateEntity.setPassword(encodedPassword);

    return this.candidateRepository.save(candidateEntity);
  }

}
