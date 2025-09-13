package com.RadixLogos.DsCatalog.controller;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.service.CategoryService;
import com.RadixLogos.DsCatalog.service.ProductService;
import com.RadixLogos.DsCatalog.test.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // Ignoring security filter
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private CategoryService categoryService;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp(){
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        when(productService.findAll(any(),any())).thenReturn(page);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        //Request
        mockMvc.perform(
                get("/products")
                        //Result format
                        .accept(MediaType.APPLICATION_JSON))
                //Assertion
                .andExpect(status().isOk());
    }
}
