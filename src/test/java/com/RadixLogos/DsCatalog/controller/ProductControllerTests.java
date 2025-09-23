package com.RadixLogos.DsCatalog.controller;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.entities.Product;
import com.RadixLogos.DsCatalog.service.CategoryService;
import com.RadixLogos.DsCatalog.service.ProductService;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import com.RadixLogos.DsCatalog.test.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // Ignoring security filter
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private CategoryService categoryService;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private Product productUpdate;
    private Product unexistingProductUpdate;
    private ProductDTO productDTO4Update;
    private ProductDTO unexistingProductDTO4Update;
    private Long existingId;
    private Long unexistingId;
    private Long dependingId;
    @BeforeEach
    void setUp(){
        existingId = 1L;
        unexistingId = 2L;
        dependingId = 3L;
        productDTO = Factory.createProductDTO(existingId);

        productUpdate = Factory.createProductWithNullId();
        productUpdate.setId(existingId);

        unexistingProductUpdate = Factory.createProductWithNullId();
        unexistingProductUpdate.setId(unexistingId);

        productDTO4Update = ProductDTO.fromProduct(productUpdate);
        unexistingProductDTO4Update = ProductDTO.fromProduct(unexistingProductUpdate);

        page = new PageImpl<>(List.of(productDTO));
        when(productService.updateProduct(productDTO4Update)).thenReturn(productDTO4Update);
        when(productService.updateProduct(unexistingProductDTO4Update)).thenThrow(new NotFoundException("Produto não encontrado"));
        when(productService.findProductById(existingId)).thenReturn(productDTO);
        when(productService.findProductById(unexistingId)).thenThrow(NotFoundException.class);
        when(productService.findAll(any(),any())).thenReturn(page);
        when(productService.insertProduct(any())).thenReturn(productDTO);
        doNothing().when(productService).deleteProduct(existingId);
        doThrow(NotFoundException.class).when(productService).deleteProduct(unexistingId);
        doThrow(DataIntegrityViolationException.class).when(productService).deleteProduct(dependingId);
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

    @Test
    public void findByIdShouldReturnOkWhenIdExists() throws Exception{

        mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdUnexists() throws Exception{
        mockMvc.perform(get("/products/" + unexistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProductShouldReturnProductDTOWhenIdExists() throws Exception{
        String objJson = objMapper.writeValueAsString(productDTO4Update);
        mockMvc.perform(put("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objJson)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void updateProductShouldThrowNotFoundExceptionWhenUnexistingId() throws Exception{
        String jsonObjUnexisting = objMapper.writeValueAsString(unexistingProductDTO4Update);
        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObjUnexisting)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void insertProductShouldReturnProductDTOWhenExistingId() throws Exception{
        String jsonBody = objMapper.writeValueAsString(productDTO);
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void deleteShouldDoNothingWhenExistingId() throws Exception{
        mockMvc.perform(delete("/products/" + existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowNotFoundExceptionWhenUnexistingId() throws Exception{
        mockMvc.perform(delete("/products/" + unexistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependingId() throws Exception{
        mockMvc.perform(delete("/products/" + dependingId))
                .andExpect(status().isBadRequest());
    }


}
