package com.deborger.shopme.admin.user;


import com.deborger.shopme.common.entity.Role;
import com.deborger.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User testUser = new User("borgepl@me.com","P2020","Pascal","De Borger");
        testUser.addRole(roleAdmin);
        User savedUser = userRepository.save(testUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
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
}
