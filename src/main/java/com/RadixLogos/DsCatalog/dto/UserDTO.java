package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.entities.Role;
import com.RadixLogos.DsCatalog.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public record UserDTO(
         Long id,
         @NotBlank(message = "O primeiro nome não pode estar em branco!")
         @Size(max = 8)
         String firstName,
         @NotBlank(message = "O segundo nome não pode ser me branco!")
         String lastName,
         @Email(message = "Email inválido!")
         String email,
         List<RoleDTO> roles
) {
    public static UserDTO fromUser(User user){
        List <RoleDTO> roles = new ArrayList<>();
        user.getAuthorities().forEach(a ->{
            roles.add(RoleDTO.fromRole((Role) a));
        });
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),roles);
    }
}
