package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.CategoryDTO;
import com.RadixLogos.DsCatalog.entities.Category;
import com.RadixLogos.DsCatalog.repositories.CategoryRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable, String name){

        return categoryRepository.findAllCategories(pageable,name).map(CategoryDTO::fromCategory);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        return CategoryDTO.fromCategory(categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("Categoria não encontrada")));
    }
    @Transactional
    public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
            var categoryEntity = new Category();
            copyDtoToEntity(categoryDTO, categoryEntity);
            return CategoryDTO.fromCategory(categoryRepository.save(categoryEntity));

    }
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO catDTO){
        if (!categoryRepository.existsById(id)){
            throw new NotFoundException("Categoria não encontrada");
        }
        var catEnt = categoryRepository.getReferenceById(id);
        copyDtoToEntity(catDTO,catEnt);
        categoryRepository.save(catEnt);
        return CategoryDTO.fromCategory(catEnt);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada");
        }
        categoryRepository.deleteById(id);
    }
    private void copyDtoToEntity(CategoryDTO categoryDTO, Category categoryEntity) {
        categoryEntity.setName(categoryDTO.name());
    }
}
