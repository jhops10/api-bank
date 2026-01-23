package com.jhops10.bank.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateWalletDto(@NotBlank @CPF String cpf,
                              @NotBlank @Email String email,
                              @NotBlank String name) {
}
