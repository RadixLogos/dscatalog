package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.entities.Category;
import com.RadixLogos.DsCatalog.entities.Product;

import java.util.ArrayList;
import java.util.List;

public record CategoryDTO(Long id, String name) {

    public static CategoryDTO fromCategory(Category category){

        return new CategoryDTO(category.getId(), category.getName());
    }
}
