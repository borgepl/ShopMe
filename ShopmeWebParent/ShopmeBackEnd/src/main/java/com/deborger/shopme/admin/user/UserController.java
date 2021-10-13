package com.deborger.shopme.admin.user;

import com.deborger.shopme.common.entity.Role;
import com.deborger.shopme.common.entity.User;
import com.deborger.shopme.admin.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/all")
    public String listAll(Model model) {
        List<User> userList = userService.listAll();
        model.addAttribute("listUsers",userList);
        return "users";
    }

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return listByPage(1,model, "firstName","asc",null);
    }

    @GetMapping("/users/page/{pageId}")
    public String listByPage(@PathVariable(name = "pageId") Integer pageNum, Model model,
        @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword ) {
        Page<User> page = userService.listByPage(pageNum,sortField,sortDir,keyword);
        List<User> userList = page.getContent();
//        System.out.println("PageNum : " + pageNum);
//        System.out.println("Total elements : " + page.getTotalElements());
//        System.out.println("Total pages : " + page.getTotalPages());
        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        // Model attributes for pagination
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        // Model attributes for sorting and filtering
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("keyword",keyword);

        // Model for the table content
        model.addAttribute("listUsers",userList);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle","Create New User");
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User theUser, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        //System.out.println(theUser);
        //System.out.println(multipartFile.getOriginalFilename());
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theUser.setPhotos(fileName);
            User savedUser = userService.save(theUser);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir,fileName,multipartFile);
        } else {
            if (theUser.getPhotos().isEmpty()) {
                theUser.setPassword(null);
            }
            userService.save(theUser);
        }

        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return getRedirectedUrlForAffectedUser(theUser);

    }

    private String getRedirectedUrlForAffectedUser(User theUser) {
        String firstPartofEmail = theUser.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartofEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit User (ID : " + id + ")" );
            return "user_form";

        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","The User ID " + id + " has been successfully deleted.");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String UpdateUserEnabledStatus(@PathVariable(name = "id") Integer id,
                                          @PathVariable(name = "status") Boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAllSorted();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(users,response);

    }

}
