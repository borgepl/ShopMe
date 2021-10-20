package com.deborger.shopme.admin.user;

import com.deborger.shopme.admin.FileUploadUtils;
import com.deborger.shopme.admin.security.ShopmeUserDetails;
import com.deborger.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewAccountDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user",user);
        model.addAttribute("pageTitle","Your Account Details");
        return "account_form";
    }

    @PostMapping("/account/update")
    public String updateUserDetails(@ModelAttribute("user") User theUser, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        //System.out.println(theUser);
        //System.out.println(multipartFile.getOriginalFilename());
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theUser.setPhotos(fileName);
            User savedUser = userService.updateAccount(theUser);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir,fileName,multipartFile);
        } else {
            if (theUser.getPhotos().isEmpty()) {
                theUser.setPassword(null);
            }
            userService.updateAccount(theUser);
        }

        loggedUser.setFirstName(theUser.getFirstName());
        loggedUser.setLasstName(theUser.getLastName());

        redirectAttributes.addFlashAttribute("message","Your account has been updated successfully.");
        return "redirect:/account";

    }


}
