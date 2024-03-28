package dev.eduardylopes.gestao_vagas.modules.company.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyRequestDTO {

  private String username;
  private String password;

}
