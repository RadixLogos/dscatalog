package com.RadixLogos.DsCatalog.dto.projections;

import com.RadixLogos.DsCatalog.entities.Role;

import java.util.List;

public interface UserProjection {
    String getEmail();
    String getPassword();
    List<Role> getAuthorities();
}
