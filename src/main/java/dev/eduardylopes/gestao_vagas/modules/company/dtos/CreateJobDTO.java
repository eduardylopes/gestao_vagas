package dev.eduardylopes.gestao_vagas.modules.company.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJobDTO {

  @Schema(example = "Vaga para pessoa desenvolvedora júnior", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "O campo [description] é obrigatório")
  private String description;

  @Schema(example = "GymPass, Plano de saúde", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "O campo [benefits] é obrigatório")
  private String benefits;

  @Schema(example = "Júnior", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "O campo [level] é obrigatório")
  private String level;

}
