package com.deborger.shopme.admin.category;

import com.deborger.shopme.admin.FileUploadUtils;
import com.deborger.shopme.admin.user.UserNotFoundException;
import com.deborger.shopme.common.entity.Category;
import com.deborger.shopme.common.entity.Role;
import com.deborger.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(@Param("sortDir") String sortDir, Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        List<Category> categories = categoryService.listAll(sortDir);
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("listCategories",categories);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
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

    @GetMapping("/categories/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            Category category = categoryService.get(id);
            List<Category> categoryList = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", categoryList);
            model.addAttribute("category",category);
            model.addAttribute("pageTitle","Edit Category (ID : " + id + ")" );
            return "categories/category_form";

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String UpdateCategoryEnabledStatus(@PathVariable(name = "id") Integer id,
                                          @PathVariable(name = "status") Boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Category ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            categoryService.delete(id);
            String categoryDir = "category-images/" + id;
            FileUploadUtils.removeDir(categoryDir);
            redirectAttributes.addFlashAttribute("message","The Category ID " + id + " has been successfully deleted.");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/categories";
    }
}
