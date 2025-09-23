package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.repositories.ProductRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long unexistingId;
    private Long count;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        unexistingId = 1000L;
        count = 25L;
    }

    @Test
    public void deleteProductShouldRemoveProductFromDatabaseWhenExistingId(){
        productService.deleteProduct(existingId);
        Assertions.assertEquals(count -1, productRepository.count());
    }

    @Test
    public void deleteProductShouldThrowNotFoundExceptionWhenUnexistingId(){
        Assertions.assertThrows(NotFoundException.class, ()->{
            productService.deleteProduct(unexistingId);
        });
    }
}
