package dev.eduardylopes.gestao_vagas.exceptions;

public class CompanyNotFoundException extends RuntimeException {
  public CompanyNotFoundException() {
    super("Empresa não encontrada");
  }
}
