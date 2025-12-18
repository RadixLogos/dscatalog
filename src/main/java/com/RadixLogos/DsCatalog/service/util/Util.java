package com.RadixLogos.DsCatalog.service.util;

import com.RadixLogos.DsCatalog.dto.projections.IdProjection;
import com.RadixLogos.DsCatalog.dto.projections.ProductProjection;
import com.RadixLogos.DsCatalog.entities.Product;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    public static <ID> List<? extends IdProjection<ID>> orderBasedOn(Page<? extends IdProjection<ID>> base, List<? extends IdProjection<ID>> toOrder) {

        Map<ID,IdProjection<ID>> map = new HashMap<>();
        for(IdProjection<ID> obj : toOrder){
            map.put(obj.getId(),obj);
        }

        List<IdProjection<ID>> orderedProduct = new ArrayList<>();
        for(IdProjection<ID> obj : base){
             orderedProduct.add(map.get(obj.getId()));
         }
    return orderedProduct;
    }
}
