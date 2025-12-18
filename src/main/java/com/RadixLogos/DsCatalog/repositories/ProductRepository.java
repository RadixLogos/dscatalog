package com.RadixLogos.DsCatalog.repositories;

import com.RadixLogos.DsCatalog.dto.projections.ProductProjection;
import com.RadixLogos.DsCatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(value = "SELECT obj " +
            "FROM Product obj JOIN FETCH obj.categories " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT(:name,'%'))",
        countQuery = "SELECT COUNT(DISTINCT obj) FROM Product obj JOIN obj.categories"
    )
    public Page<Product> findAllProducts(Pageable pageable, String name);

    //To make the search with the asked requirement from the front-end page, we've put this new method with plain SQL
    @Query(nativeQuery = true, value = """
            SELECT * FROM(
            SELECT DISTINCT tb_product.id,tb_product.name
            FROM tb_product
            INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
            WHERE LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:productName,'%'))
            AND (:categoryIds IS NULL OR tb_product_category.category_id IN :categoryIds)
            ) AS tb_result
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM(
            SELECT DISTINCT tb_product.id,tb_product.name
            FROM tb_product
            INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
            WHERE LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:productName,'%'))
            AND (:categoryIds IS NULL OR tb_product_category.category_id IN :categoryIds)
            )AS tb_result
            """
    )
    public Page<ProductProjection> searchAllProducts(List<Long> categoryIds, String productName, Pageable pageable);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds")
    public List<Product> searchAllProductsWithCategories(List<Long> productIds);

}
