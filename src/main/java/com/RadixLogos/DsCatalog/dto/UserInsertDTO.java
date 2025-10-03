package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.service.validation.UserInsertValid;

@UserInsertValid
public record UserInsertDTO(UserDTO userDTO, String password) {
}
