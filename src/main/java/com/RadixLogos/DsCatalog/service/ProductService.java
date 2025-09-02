package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.entities.Category;
import com.RadixLogos.DsCatalog.entities.Product;
import com.RadixLogos.DsCatalog.repositories.CategoryRepository;
import com.RadixLogos.DsCatalog.repositories.ProductRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable, String name){
        var products = productRepository.findAllProducts(pageable, name);
        return products.map(ProductDTO::fromProduct);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id){
        var product = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product not found"));
        return ProductDTO.fromProduct(product);
    }

    @Transactional
    public ProductDTO insertProduct(ProductDTO productDTO){
        var product = new Product();
        copyDtoToEntity(productDTO,product);
        product = productRepository.save(product);
        return ProductDTO.fromProduct(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO){
        if(!productRepository.existsById(id)){
            throw new NotFoundException("Produto não encontrado");
        }
        var product = productRepository.getReferenceById(id);
        copyDtoToEntity(productDTO,product);
        return ProductDTO.fromProduct(product);
    }

    @Transactional
    public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new NotFoundException("Produto não encontrado");
        }
        productRepository.deleteById(id);
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product product) {
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setImgUrl(productDTO.imgUrt());
        product.getCategories().clear();
        productDTO.categories().forEach(catDTO -> {
            var category = categoryRepository.findById(catDTO.id())
                    .orElseThrow(()-> new NotFoundException("Categoria não encontrada"));
            product.addCategory(category);
        });
    }
}
