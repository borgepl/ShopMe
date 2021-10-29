package com.deborger.shopme.admin.category;

import com.deborger.shopme.admin.user.UserNotFoundException;
import com.deborger.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");
        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }
        List<Category> rootCategories = categoryRepository.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories,sortDir);
    }

    public List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories =  new ArrayList<>();
           for (Category rootCategory : rootCategories) {
               hierarchicalCategories.add(Category.copyFull(rootCategory));
               Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);
               for (Category subCategory : children) {
                   String newName = "--" + subCategory.getName();
                   hierarchicalCategories.add(Category.copyFull(subCategory,newName));
                   listSubHierarchicalCategories(hierarchicalCategories,subCategory,1,sortDir);
               }

           }
        return hierarchicalCategories;
    }

    public void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
        int newSubLevel = subLevel + 1;
        for (Category subCategory : sortSubCategories(parent.getChildren(),sortDir)) {
            String newName = "";
            for (int i = 0; i < newSubLevel; i++) {
                newName += "--";
            }
            newName += subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory,newName));
            listSubHierarchicalCategories(hierarchicalCategories,subCategory,newSubLevel,sortDir);
        }

    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null ) {
                categoriesUsedInForm.add(Category.CopyIdAndName(category));
                //System.out.println(category.getName());
                Set<Category> children = sortSubCategories(category.getChildren());
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
        Set<Category> children = sortSubCategories(parent.getChildren());
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

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children,"asc");
    }

        private SortedSet<Category> sortSubCategories(Set<Category> children , String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, Boolean enabled) {
        categoryRepository.updateEnabledStatus(id,enabled);
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
        categoryRepository.deleteById(id);
    }
}
