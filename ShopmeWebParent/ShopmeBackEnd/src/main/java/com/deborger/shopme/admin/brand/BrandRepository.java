package com.deborger.shopme.admin.brand;

import com.deborger.shopme.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    public Brand findByName(String name);

    Long countById(Integer id);
}