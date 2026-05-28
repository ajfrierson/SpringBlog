package org.studyeasy.SpringBlog.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;


import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studyeasy.SpringBlog.models.Account;
import org.studyeasy.SpringBlog.services.AccountService;
import org.studyeasy.SpringBlog.util.AppUtil;
import org.springframework.util.*;


@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Value("${spring.mvc.static-path-pattern}")
    private String photoPrefix;

    @GetMapping("/register")
    public String register(Model model){
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result){
        if(result.hasErrors()){
            return "account_views/register";
        }
        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal){
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            model.addAttribute("timestamp", System.currentTimeMillis()); 
            return "account_views/profile";
        }else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal){
        if(result.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent()) {
            Account accountById = accountService.findById(account.getId()).get();
            accountById.setAge(account.getAge());
            accountById.setDate_of_birth(account.getDate_of_birth());
            accountById.setFirstname(account.getFirstname());
            accountById.setLastname(account.getLastname());
            accountById.setGender(account.getGender());
            accountById.setPassword(account.getPassword());

            accountService.save(accountById);
            SecurityContextHolder.clearContext();
            return "redirect:/";
        }else {
                return "redirect:/?error";
        }
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(@RequestParam("file") 
    MultipartFile file, RedirectAttributes attributes, 
    Principal principal) {
        if(file.isEmpty()) {
            attributes.addFlashAttribute("error", "No file uploaded");
            return "redirect:/profile";
        }else {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                // handle the case — return early, throw exception, or use a default name
                throw new IllegalArgumentException("File name cannot be null or empty");
            }

            try {
                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;
                String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                String finalPhotoName = generatedString + originalFilename;
                String fileLocation = AppUtil.getUploadPath(finalPhotoName);

                Path path = Paths.get(fileLocation);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message", "You successfully uploaded");

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();
                }

                Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                if(optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();
                    Account accountById = accountService.findById(account.getId()).get();
                    String photoFilePath = photoPrefix.replace("**", "uploads/" + finalPhotoName);
                    accountById.setPhoto(photoFilePath);
                    accountService.save(accountById);
                }
                return "redirect:/profile";
                
            } catch (Exception e) {
                e.printStackTrace();
                attributes.addFlashAttribute("error", "Failed to upload photo: " + e.getMessage());
                return "redirect:/profile";  // ✅ Always redirect, never return ""
                }
        }
    }

    
}
