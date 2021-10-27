package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("select c from Category c where c.parent.id is null")
    public List<Category> findRootCategories();

}