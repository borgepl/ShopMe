package com.deborger.shopme.admin.user;

import com.deborger.shopme.common.entity.Role;
import com.deborger.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model) {
        List<User> userList = userService.listAll();
        model.addAttribute("listUsers",userList);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User theUser, RedirectAttributes redirectAttributes) {
        userService.save(theUser);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return "redirect:/users";
    }
}
