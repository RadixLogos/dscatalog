package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.CategoryDTO;
import com.RadixLogos.DsCatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        return categoryRepository.findAll().stream().map(CategoryDTO::fromCategory).toList();
    }
}
