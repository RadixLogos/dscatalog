package com.RadixLogos.DsCatalog.test;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.entities.Product;
import com.RadixLogos.DsCatalog.repositories.ProductRepository;

import java.time.Instant;

public class Factory {

    public static Product createProductWithNullId(){
        return new Product(null,"Bola de basquete","Uma bola normal", Instant.now(),25.50,"http://boladebasquete.com");
    }

    public static ProductDTO createProductDTO(Long id){
        var product = createProductWithNullId();
        product.setId(id);
        return ProductDTO.fromProduct(product);
    }
}
