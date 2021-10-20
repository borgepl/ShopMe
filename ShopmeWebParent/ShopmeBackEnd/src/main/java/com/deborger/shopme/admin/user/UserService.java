package com.deborger.shopme.admin.user;

import com.deborger.shopme.common.entity.Role;
import com.deborger.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public List<User> listAllSorted() {
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public Page<User> listByPage(Integer pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
        if (keyword != null) {
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User theUser) {
        boolean isUpdatingUser = (theUser.getId() != null);
        if (isUpdatingUser) {
            User userExisting = userRepository.findById(theUser.getId()).get();
            if (theUser.getPassword().isEmpty()) {
                theUser.setPassword(userExisting.getPassword());
            } else {
                encodePassword(theUser);
            }
        } else {
            encodePassword(theUser);
        }
        return userRepository.save(theUser);

    }

    public User updateAccount(User userInForm) {
        Optional<User> userOptional = userRepository.findById(userInForm.getId());

            User userInDb = userOptional.get();
            if (!userInForm.getPassword().isEmpty()) {
                userInDb.setPassword(userInForm.getPassword());
                encodePassword(userInDb);
            }
            if (userInForm.getPhotos() != null) {
                userInDb.setPhotos(userInForm.getPhotos());
            }
            userInDb.setFirstName(userInForm.getFirstName());
            userInDb.setLastName(userInForm.getLastName());

            return userRepository.save(userInDb);
    }

    private void encodePassword(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public Boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) return true;
        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id ) {
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, Boolean enabled) {
        userRepository.updateEnabledStatus(id,enabled);
    }

}

