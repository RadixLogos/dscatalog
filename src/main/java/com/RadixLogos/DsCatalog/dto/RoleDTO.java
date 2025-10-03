package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.entities.Role;

public record RoleDTO(Long id, String authority) {
    public static RoleDTO fromRole(Role role){
        return new RoleDTO(role.getId(), role.getAuthority());
    }
}
