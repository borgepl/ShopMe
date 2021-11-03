package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("select c from Category c where c.parent.id is null")
    public List<Category> findRootCategories(Sort sort);

    @Query("select c from Category c where c.parent.id is null")
    public Page<Category> findRootCategories(Pageable pageable);

    public Category findByName(String name);

    public Category findByAlias(String alias);

    @Query("update Category c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, Boolean enabled);

    public Long countById(Integer id);
}