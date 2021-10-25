package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null ) {
                categoriesUsedInForm.add(new Category(category.getName()));
                //System.out.println(category.getName());
                Set<Category> children = category.getChildren();
                for (Category child : children) {
                    String newName = "--" + child.getName();
                    categoriesUsedInForm.add(new Category(newName));
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
            categoriesUsedInForm.add(new Category(newName));
            listChildren(categoriesUsedInForm,child,newSubLevel);
        }
    }

}
