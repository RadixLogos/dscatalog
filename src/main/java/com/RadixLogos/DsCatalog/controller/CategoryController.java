package com.RadixLogos.DsCatalog.controller;

import com.RadixLogos.DsCatalog.dto.CategoryDTO;
import com.RadixLogos.DsCatalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
