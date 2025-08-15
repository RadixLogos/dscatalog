package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.ProductDTO;
import com.RadixLogos.DsCatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable, String name){
        var products = productRepository.findAllProducts(pageable, name);
        return products.map(ProductDTO::fromProduct);
    }
}
