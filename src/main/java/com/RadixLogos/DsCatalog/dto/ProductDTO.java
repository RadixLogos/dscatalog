package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.entities.Product;
import jakarta.validation.constraints.NotEmpty;

public record ProductDTO(
        Long id,
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        Double price,
        String imgUrt
) {
    public static ProductDTO fromProduct(Product product){
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl());
    }

}
