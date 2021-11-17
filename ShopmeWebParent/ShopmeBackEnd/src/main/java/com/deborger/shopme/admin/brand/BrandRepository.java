package com.deborger.shopme.admin.brand;

import com.deborger.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    public Brand findByName(String name);

    public Long countById(Integer id);

    @Query("select b from Brand b where b.name like %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);

    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    //@Query("select b from Brand b order by b.name ASC")
    public List<Brand> findAll();
}