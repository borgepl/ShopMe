package com.deborger.shopme.admin.brand;

import com.deborger.shopme.admin.category.CategoryService;
import com.deborger.shopme.common.entity.Brand;
import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model) {
        List<Brand> brandList = brandService.listAll();
        model.addAttribute("listBrands",brandList);
        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();
        model.addAttribute("listCategories", categoryList);
        model.addAttribute("pageTitle","Create New Brand");
        model.addAttribute("brand", new Brand());
        return "brands/brand_form";
    }

}
