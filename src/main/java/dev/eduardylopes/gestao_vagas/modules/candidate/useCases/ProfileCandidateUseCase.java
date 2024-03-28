package dev.eduardylopes.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.eduardylopes.gestao_vagas.modules.candidate.dtos.ProfileCandidateResponseDTO;
import dev.eduardylopes.gestao_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class ProfileCandidateUseCase {

  @Autowired
  private CandidateRepository candidateRepository;

  public ProfileCandidateResponseDTO execute(UUID idCandidate) {
    var candidate = this.candidateRepository.findById(idCandidate).orElseThrow(() -> {
      throw new UsernameNotFoundException("User not found");
    });

    return ProfileCandidateResponseDTO.builder()
        .id(candidate.getId())
        .description(candidate.getDescription())
        .email(candidate.getEmail())
        .name(candidate.getName())
        .username(candidate.getUsername())
        .build();
  }
}
