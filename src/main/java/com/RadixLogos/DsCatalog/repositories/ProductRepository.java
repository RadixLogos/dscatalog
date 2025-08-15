package com.RadixLogos.DsCatalog.repositories;

import com.RadixLogos.DsCatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT obj " +
            "FROM Product obj " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT(:name,'%'))")
    public Page<Product> findAllProducts(Pageable pageable, String name);
}
