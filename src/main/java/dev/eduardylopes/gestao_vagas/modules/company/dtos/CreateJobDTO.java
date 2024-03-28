package dev.eduardylopes.gestao_vagas.modules.company.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJobDTO {

  @NotBlank(message = "O campo [description] é obrigatório")
  private String description;

  @NotBlank(message = "O campo [benefits] é obrigatório")
  private String benefits;

  @NotBlank(message = "O campo [level] é obrigatório")
  private String level;

}
