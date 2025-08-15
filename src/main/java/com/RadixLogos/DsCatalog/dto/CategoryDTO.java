package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.entities.Category;
import com.RadixLogos.DsCatalog.entities.Product;

import java.util.ArrayList;
import java.util.List;

public record CategoryDTO(Long id, String name, List<ProductDTO> products) {

    public static CategoryDTO fromCategory(Category category){
       List<ProductDTO> products = new ArrayList<>();
       category.getProducts().forEach(p ->{
           products.add(ProductDTO.fromProduct(p));
       });
        return new CategoryDTO(category.getId(), category.getName(),products);
    }
}
