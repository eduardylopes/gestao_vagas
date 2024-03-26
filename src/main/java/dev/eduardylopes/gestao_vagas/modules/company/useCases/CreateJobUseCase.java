package dev.eduardylopes.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eduardylopes.gestao_vagas.exceptions.CompanyNotFoundException;
import dev.eduardylopes.gestao_vagas.modules.company.entities.JobEntity;
import dev.eduardylopes.gestao_vagas.modules.company.repositories.CompanyRepository;
import dev.eduardylopes.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class CreateJobUseCase {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private CompanyRepository companyRepository;
  
  public JobEntity execute(JobEntity jobEntity) {

    var companyEntity = this.companyRepository.findById(jobEntity.getCompanyId());

    if (companyEntity.isEmpty()) {
      throw new CompanyNotFoundException();
    }

    return this.jobRepository.save(jobEntity);
  }

}
