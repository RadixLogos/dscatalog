package com.RadixLogos.DsCatalog.controller;

import com.RadixLogos.DsCatalog.dto.CategoryDTO;
import com.RadixLogos.DsCatalog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAllCategories(){
        var response = categoryService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        var response = categoryService.insertCategory(categoryDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryDTO.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
