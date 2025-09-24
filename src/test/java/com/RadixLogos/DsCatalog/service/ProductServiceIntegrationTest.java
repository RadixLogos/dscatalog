package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.repositories.ProductRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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
        Assertions.assertEquals(count-1, productRepository.count());
    }

    @Test
    public void deleteProductShouldThrowNotFoundExceptionWhenUnexistingId(){
        Assertions.assertThrows(NotFoundException.class, ()->{
            productService.deleteProduct(unexistingId);
        });
    }

    @Test
    public void findAllShouldReturnPageWhenPageZeroWithSizeTen(){
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<ProductDTO> result = productService.findAll(pageRequest,"");
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0,result.getNumber());
        Assertions.assertEquals(10,result.getSize());
        Assertions.assertEquals(count,result.getTotalElements());
    }

 @Test
    public void findAllShouldReturnEmptyPageWhenPageFifty(){
        PageRequest pageRequest = PageRequest.of(50,10);
        Page<ProductDTO> result = productService.findAll(pageRequest,"");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortedByName(){
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("name"));
        Page<ProductDTO> result = productService.findAll(pageRequest,"");
        Assertions.assertEquals("Macbook Pro", result.getContent().getFirst().name());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).name());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).name());
    }

    @Test
    public void findAllShouldReturnPageWithNameFilteredWhenSearchedByName(){
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<ProductDTO> result = productService.findAll(pageRequest,"The ");
        Assertions.assertEquals("The Lord of the Rings", result.getContent().getFirst().name());
    }


}
