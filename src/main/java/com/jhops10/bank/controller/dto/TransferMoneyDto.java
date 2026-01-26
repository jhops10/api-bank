package com.jhops10.bank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferMoneyDto(@NotNull UUID sender,
                               @NotNull @DecimalMin("0.01") BigDecimal transferValue,
                               @NotNull UUID receiver) {
}
