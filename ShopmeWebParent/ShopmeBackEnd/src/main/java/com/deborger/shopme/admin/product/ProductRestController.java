package com.deborger.shopme.admin.product;

import com.deborger.shopme.admin.brand.BrandNotFoundRestException;
import com.deborger.shopme.admin.brand.BrandService;
import com.deborger.shopme.admin.brand.CategoryDTO;
import com.deborger.shopme.admin.category.CategoryNotFoundException;
import com.deborger.shopme.common.entity.Brand;
import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return productService.checkUnique(id, name);
    }

}
