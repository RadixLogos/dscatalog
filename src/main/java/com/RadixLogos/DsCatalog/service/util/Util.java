package com.RadixLogos.DsCatalog.service.util;

import com.RadixLogos.DsCatalog.dto.projections.ProductProjection;
import com.RadixLogos.DsCatalog.entities.Product;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    public static List<Product> orderBasedOn(Page<ProductProjection> base, List<Product> toOrder) {

        Map<Long, Product> map = new HashMap<>();
        for(Product obj : toOrder){
            map.put(obj.getId(),obj);
        }

        List<Product> orderedProduct = new ArrayList<>();
        for(ProductProjection obj : base){
             orderedProduct.add(map.get(obj.getId()));
         }
    return orderedProduct;
    }
}
