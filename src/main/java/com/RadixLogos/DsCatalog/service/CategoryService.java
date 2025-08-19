package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.CategoryDTO;
import com.RadixLogos.DsCatalog.entities.Category;
import com.RadixLogos.DsCatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        return categoryRepository.findAll().stream().map(CategoryDTO::fromCategory).toList();
    }

    @Transactional
    public CategoryDTO insertCategory(CategoryDTO categoryDTO) {

            var categoryEntity = new Category();
            copyDtoToEntity(categoryDTO, categoryEntity);
            return CategoryDTO.fromCategory(categoryRepository.save(categoryEntity));

    }
    private void copyDtoToEntity(CategoryDTO categoryDTO, Category categoryEntity) {
        categoryEntity.setId(categoryDTO.id());
        categoryEntity.setName(categoryDTO.name());
        categoryEntity.setCreatedAt(Instant.now());
    }
}
