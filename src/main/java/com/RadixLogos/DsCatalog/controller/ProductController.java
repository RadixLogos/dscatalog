package com.RadixLogos.DsCatalog.controller;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ProductDTO> findAllProducts(
            Pageable pageable,
            @RequestParam(name = "name", defaultValue = "") String name){
        return productService.findAll(pageable,name);
    }
}
