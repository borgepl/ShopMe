package com.deborger.shopme.admin.user;


import com.deborger.shopme.common.entity.Role;
import com.deborger.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    //@Test
    public void testCreateUser() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User testUser = new User("borgepl@me.com","P2020","Pascal","De Borger");
        testUser.addRole(roleAdmin);
        User savedUser = userRepository.save(testUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    //@Test
    public void testCreateUserWithTwoRoles() {

        User testUser2 = new User("Ravi@me.com","P2020","Rave","Kumar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        testUser2.addRole(roleEditor);
        testUser2.addRole(roleAssistant);
        User savedUser = userRepository.save(testUser2);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user));

    }

    @Test
    public void testGetUserById() {
        Optional<User> testOptional = userRepository.findById(1);
        if (testOptional.isPresent()) {
            User testUser = testOptional.get();
            assertThat(testUser).isNotNull();
        }
    }

    @Test
    public void testGetUserByEmail() {
        String email = "borgepl@me.com";
        User userByEmail = userRepository.getUserByEmail(email);

        assertThat(userByEmail).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long count = userRepository.countById(id);
        assertThat(count).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id,false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id,true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> userList = page.getContent();
        userList.forEach(System.out::println);
        assertThat(userList.size()).isEqualTo(pageSize);
    }

    @Test
    public void testFilterSearchUsers() {
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(keyword,pageable);
        List<User> userList = page.getContent();
        userList.forEach(System.out::println);
        assertThat(userList.size()).isGreaterThan(0);
    }
}
