package com.deborger.shopme.admin.brand;

import com.deborger.shopme.admin.FileUploadUtils;
import com.deborger.shopme.admin.category.CategoryNotFoundException;
import com.deborger.shopme.admin.category.CategoryService;
import com.deborger.shopme.common.entity.Brand;
import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @PostMapping("/brands/save")
    public String saveBrand(@ModelAttribute("brand") Brand brand, RedirectAttributes redirectAttributes,
                              @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);
            String uploadDir = "brand-images/" + savedBrand.getId();
            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir,fileName,multipartFile);
        } else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message","The brand has been saved successfully.");
        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            Brand brand = brandService.get(id);
            List<Category> categoryList = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", categoryList);
            model.addAttribute("brand",brand);
            model.addAttribute("pageTitle","Edit Brand (ID : " + id + ")" );
            return "brands/brand_form";

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            brandService.delete(id);
            String brandDir = "brand-images/" + id;
            FileUploadUtils.removeDir(brandDir);
            redirectAttributes.addFlashAttribute("message","The Brand ID " + id + " has been successfully deleted.");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/brands";
    }


}
