package com.deborger.shopme.admin.category;

import com.deborger.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    //@Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics","electronics");
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    //@Test
    public void testCreateSubCategory() {
        Category parent = new Category(1);
        Category subCat = new Category("Desktops",parent);
        Category savedSubCat = categoryRepository.save(subCat);

        assertThat(savedSubCat.getId()).isGreaterThan(0);

    }

    //@Test
    public void testCreateOtherSubCategory() {
        Category parent = new Category(1);
        Category laptops = new Category("Laptops",parent);
        Category components = new Category("Components",parent);
        categoryRepository.save(laptops);
        categoryRepository.save(components);
    }

    //@Test
    public void testCreateOthersSubCategory() {
        Category parent = new Category(2);
        Category cameras = new Category("Cameras",parent);
        Category smartphone = new Category("Smartphones",parent);
        categoryRepository.save(cameras);
        categoryRepository.save(smartphone);
    }

    //@Test
    public void testCreateThirdSubCategory() {
        Category parent = new Category(5);
        Category memory = new Category("Memory",parent);
        categoryRepository.save(memory);

    }

    @Test
    public void testGetCategory() {
        Category category = categoryRepository.findById(1).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        for (Category subCat : children) {
            System.out.println(subCat.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        for (Category subCat : categories) {
            if (subCat.getParent() == null ) {
                System.out.println(subCat.getName());
                Set<Category> children = subCat.getChildren();
                for (Category child : children) {
                    System.out.println("--" + child.getName());
                    printChildren(child,1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category child : children) {
            for (int i = 0; i < newSubLevel; i++) {
            System.out.print("--");
            }
            System.out.println(child.getName());
            printChildren(child,newSubLevel);
        }
    }

    @Test
    public void testListRootCategories() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        for (Category rootCategory : rootCategories) {
            String name = rootCategory.getName();
            System.out.println(name);
        }
    }

    @Test
    public void testFindByName() {
        String name = "Computers";
        Category category = categoryRepository.findByName(name);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias() {
        String alias = "computers";
        Category category = categoryRepository.findByAlias(alias);
        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }
}
