package com.deborger.shopme.admin.category;

import com.deborger.shopme.admin.user.UserNotFoundException;
import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);  // in New Mode
        Category categoryByName = categoryRepository.findByName(name);
        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {  //in Editing Mode - check if not for the same id
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicateName";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }
}
