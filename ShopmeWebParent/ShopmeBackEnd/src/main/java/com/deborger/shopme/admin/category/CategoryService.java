package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    public List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories =  new ArrayList<>();
           for (Category rootCategory : rootCategories) {
               hierarchicalCategories.add(Category.copyFull(rootCategory));
               for (Category subCategory : rootCategory.getChildren()) {
                   String newName = "--" + subCategory.getName();
                   hierarchicalCategories.add(Category.copyFull(subCategory,newName));
                   listSubHierarchicalCategories(hierarchicalCategories,subCategory,1);
               }

           }
        return hierarchicalCategories;
    }

    public void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Category subCategory : parent.getChildren()) {
            String newName = "";
            for (int i = 0; i < newSubLevel; i++) {
                newName += "--";
            }
            newName += subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory,newName));
            listSubHierarchicalCategories(hierarchicalCategories,subCategory,newSubLevel);
        }

    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null ) {
                categoriesUsedInForm.add(Category.CopyIdAndName(category));
                //System.out.println(category.getName());
                Set<Category> children = category.getChildren();
                for (Category child : children) {
                    String newName = "--" + child.getName();
                    categoriesUsedInForm.add(Category.CopyIdAndName(child.getId(),newName));
                    //System.out.println("--" + child.getName());
                    listChildren(categoriesUsedInForm, child,1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm,Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category child : children) {
            String newName = "";
            for (int i = 0; i < newSubLevel; i++) {
                newName += "--";
            }
            newName += child.getName();
            categoriesUsedInForm.add(Category.CopyIdAndName(child.getId(),newName));
            listChildren(categoriesUsedInForm,child,newSubLevel);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
