package dev.eduardylopes.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.eduardylopes.gestao_vagas.exceptions.UserFoundException;
import dev.eduardylopes.gestao_vagas.modules.company.entities.CompanyEntity;
import dev.eduardylopes.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public CompanyEntity execute(CompanyEntity companyEntity) {

    this.companyRepository.findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
        .ifPresent(user -> {
          throw new UserFoundException();
        });

    var encodedPassword = this.passwordEncoder.encode(companyEntity.getPassword());
    companyEntity.setPassword(encodedPassword);

    return this.companyRepository.save(companyEntity);
  }
}
