package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
}