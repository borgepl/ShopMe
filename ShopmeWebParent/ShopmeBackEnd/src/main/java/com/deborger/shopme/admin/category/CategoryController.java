package com.deborger.shopme.admin.category;

import com.deborger.shopme.admin.FileUploadUtils;
import com.deborger.shopme.common.entity.Category;
import com.deborger.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();
        model.addAttribute("listCategories", categoryList);
        model.addAttribute("pageTitle","Create New Category");
        model.addAttribute("category", new Category());
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategoy(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes,
                           @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory =  categoryService.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();
            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir,fileName,multipartFile);
        } else {
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message","The category has been saved successfully.");
        //return getRedirectedUrlForAffectedUser(theUser);
        return "redirect:/categories";
    }
}
