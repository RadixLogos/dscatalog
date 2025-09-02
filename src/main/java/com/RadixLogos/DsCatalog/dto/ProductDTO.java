package com.RadixLogos.DsCatalog.dto;

import com.RadixLogos.DsCatalog.entities.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record ProductDTO(
        Long id,
        @NotEmpty(message = "O nome do produto é obrigatório")
        String name,
        @NotEmpty(message = "A descrição do produto é obrigatória")
        String description,
        @Positive(message = "O preço do produto deve ser maior que zero")
        Double price,
        @PastOrPresent
        Instant date,
        String imgUrt,
        @NotEmpty(message = "O produto deve vazer parte de ao menos uma categoria")
        List<CategoryDTO> categories
) {
    public static ProductDTO fromProduct(Product product){
        List<CategoryDTO> categories = new ArrayList<>();
        product.getCategories().forEach(c -> {
            categories.add(CategoryDTO.fromCategory(c));
        });
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDate(),
                product.getImgUrl(),
                categories);
    }

}
