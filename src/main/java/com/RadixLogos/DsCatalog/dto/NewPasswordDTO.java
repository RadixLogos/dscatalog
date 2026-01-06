package com.RadixLogos.DsCatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewPasswordDTO(
        @NotBlank(message = "Campo obrigatório")
        String token,
        @NotBlank(message = "Campo obrigatório")
        @Size(min = 8, message = "Senha iválida")
        String password) {
}
