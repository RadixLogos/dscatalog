package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.service.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public record UserInsertDTO(
        UserDTO userDTO,
        @NotBlank(message = "Campo requerido")
        @Size(min=8, message = "A senha precisa conter no mínimo 8 carcateres")
        String password) {
}
