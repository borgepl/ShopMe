package com.deborger.shopme.admin.brand;


import com.deborger.shopme.admin.category.CategoryRepository;
import com.deborger.shopme.common.entity.Brand;
import com.deborger.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreate() {
        Brand brand1 =  new Brand("Acer");

        Category laptops = categoryRepository.findByName("Laptops");
        Set<Category> categorySet =  new HashSet<>();
        categorySet.add(laptops);

        brand1.setCategories(categorySet);

        Brand savedBrand = brandRepository.save(brand1);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void findAllBrands() {
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(brand -> System.out.println(brand));
    }

    @Test
    public void testFindById() {
        Brand brand = brandRepository.findById(1).get();
        assertThat(brand.getName()).isEqualTo("Acer");
    }
}
