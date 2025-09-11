package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.entities.Product;
import com.RadixLogos.DsCatalog.repositories.ProductRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import com.RadixLogos.DsCatalog.test.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long unexistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    
    @BeforeEach
    void setUp(){
        existingId = 1L;
        unexistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProductWithNullId();
        product.setId(existingId);
        page = new PageImpl<>(List.of(product));

        when(productRepository.findAllProducts(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(page);
        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(unexistingId)).thenReturn(Optional.empty()).thenThrow(NotFoundException.class);
        doReturn(true).when(productRepository).existsById(existingId);
        doReturn(true).when(productRepository).existsById(dependentId);
        doReturn(false).when(productRepository).existsById(unexistingId);
        doNothing().when(productRepository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void deleteProductShouldDoNothingWhenExistingId(){
        Assertions.assertDoesNotThrow(()->{
            productService.deleteProduct(existingId);
        });
        verify(productRepository,times(1)).deleteById(existingId);
    }

    @Test
    public void deleteProductShouldThrowNotFoundExceptionWhenUnexistingId(){
        Assertions.assertThrows(NotFoundException.class,()->{
            productService.deleteProduct(unexistingId);
        });
        verify(productRepository, times(0)).deleteById(unexistingId);
    }

    @Test
    public void deleteProductShouldThrowDataIntegrityViolationExceptionWhenDependingId(){
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            productService.deleteProduct(dependentId);
        });
        verify(productRepository, times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        var pageOfProductDto = productService.findAll(pageable,"");

        Assertions.assertNotNull(pageOfProductDto);
        verify(productRepository,times(1)).findAllProducts(pageable,"");
    }
}
