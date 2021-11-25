package com.deborger.shopme.admin.product;

import com.deborger.shopme.common.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    public Product findByName(String name);
}