package com.deborger.shopme.admin.product;

import com.deborger.shopme.common.entity.Brand;
import com.deborger.shopme.common.entity.Category;
import com.deborger.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateProductOne() {
        Brand brand = testEntityManager.find(Brand.class, 10); //Samsung
        Category category = testEntityManager.find(Category.class, 15); //Unlocked Cell Phones

        Product product = new Product();
        product.setName("Samsung Galaxy A31");
        product.setAlias("samsung_galaxy_a31");
        product.setShortDescription("Short description for a good phone");
        product.setFullDescription("Full description for Samsung Galaxy A31");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(456);
        product.setCost(350);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateProductTwo() {
        Brand brand = testEntityManager.find(Brand.class, 38); //Dell
        Category category = testEntityManager.find(Category.class, 6); //Laptops

        Product product = new Product();
        product.setName("Dell Inspiron 3000");
        product.setAlias("dell_inspiron_300");
        product.setShortDescription("Short description for Dell Ins 3000");
        product.setFullDescription("Full description for Dell insp 3000");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(578);
        product.setCost(500);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateProductThree() {
        Brand brand = testEntityManager.find(Brand.class, 37);
        Category category = testEntityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short description for Acer Aspire");
        product.setFullDescription("Full description for Acer Aspire");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts() {
        Iterable<Product> iterableProducts = productRepository.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct() {
        Integer id = 2;
        Product product = productRepository.findById(id).get();
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        product.setPrice(499);

        productRepository.save(product);

        Product updatedProduct = testEntityManager.find(Product.class, id);

        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 3;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);

        assertThat(!result.isPresent());
    }

}
