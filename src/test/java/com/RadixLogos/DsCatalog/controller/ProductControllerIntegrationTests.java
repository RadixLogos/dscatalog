package com.RadixLogos.DsCatalog.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.test.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objMapper;

    private Long existingId;
    private Long unexistingId;
    private Long count;
    private ProductDTO existingProductDTO;
    private ProductDTO unexistingProductDTO;
    @BeforeEach
    void setUp(){
        existingId = 1L;
        unexistingId = 1000L;
        count = 25L;
        existingProductDTO = Factory.createProductDTO(existingId);
        unexistingProductDTO = Factory.createProductDTO(unexistingId);
    }

    @Test
    void findAllShouldReturnOrganizedElementsWhenSortedByName() throws Exception{
        mockMvc.perform(get("/products?page=0&size=12&sort=name,asc"))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(count))
                .andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.content[1].name").value("PC Gamer"))
                .andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    void updateProductShouldReturnUpdatedProductDTOWhenExistingId() throws Exception{
        String jsonBody = objMapper.writeValueAsString(existingProductDTO);
        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(existingProductDTO.id()))
        .andExpect(jsonPath("$.name").value(existingProductDTO.name()))
        .andExpect(jsonPath("$.description").value(existingProductDTO.description()));

    }
    @Test
    void updateProductShouldThrowNotFoundExceptionWhenUnexistingId() throws Exception{
        String jsonBody = objMapper.writeValueAsString(unexistingProductDTO);
        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
        .andExpect(status().isNotFound());
    }
}
