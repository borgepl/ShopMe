package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model) {
        List<Category> categories = categoryService.listAll();
        model.addAttribute("listCategories",categories);
        return "categories/categories";
    }
}
