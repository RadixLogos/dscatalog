package com.RadixLogos.DsCatalog.repositories;

import com.RadixLogos.DsCatalog.entities.Product;
import com.RadixLogos.DsCatalog.test.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    private Long existingId;
    private Long unexistingId;
    private Product product;
    private Long lastId;
    @BeforeEach
    void stUp() throws Exception{
         existingId= 1L;
         unexistingId= 1000L;
         product = Factory.createProductWithNullId();
         lastId = 25L;
    }
    @Test
    public void shouldDeleteEntityWhenExistingId(){
        productRepository.deleteById(existingId);
        Assertions.assertFalse(productRepository.existsById(existingId));
    }

    @Test
    public void shouldSaveNewProductWithIncrementedId(){
        product = productRepository.save(product);
        Assertions.assertEquals(lastId + 1,product.getId());
    }

    @Test
    public void shouldReturnOptionalWithEntityWhenExistingId(){
        Optional<Product> optionalProduct = productRepository.findById(existingId);
        Assertions.assertTrue(optionalProduct.isPresent());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUnexistingId(){
        Optional<Product> optionalProduct = productRepository.findById(unexistingId);
        Assertions.assertFalse(optionalProduct.isPresent());
    }
}
