package dev.eduardylopes.gestao_vagas.modules.candidate.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.eduardylopes.gestao_vagas.modules.candidate.entities.CandidateEntity;
import dev.eduardylopes.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import dev.eduardylopes.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

  @Autowired
  ProfileCandidateUseCase profileCandidateUseCase;

  @Autowired
  private CreateCandidateUseCase createCandidateUseCase;

  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
    try {
      var result = this.createCandidateUseCase.execute(candidateEntity);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  @PreAuthorize("hasRole('CANDIDATE')")
  public ResponseEntity<Object> get(HttpServletRequest request) {
    var candidateId = request.getAttribute("candidate_id").toString();

    try {
      var result = this.profileCandidateUseCase.execute(UUID.fromString(candidateId));
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
