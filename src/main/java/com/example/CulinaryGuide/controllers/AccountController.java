package com.example.CulinaryGuide.controllers;

import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.Authentication.Userstable;
import com.example.CulinaryGuide.models.Cooking;
import com.example.CulinaryGuide.models.Dish;
import com.example.CulinaryGuide.models.SelectedRecipes;
import com.example.CulinaryGuide.service.RoleService;
import com.example.CulinaryGuide.service.UserService;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final RoleService roleService;

    public AccountController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String accountView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        model.addAttribute("account", userService.loadUserByUsername(currentUserName));
        if(currentUserName.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(currentUserName);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",currentUserName);
        return "Authentication/personalAccount";
    }


    @GetMapping("/EditPassword")
    public String editPass(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        model.addAttribute("account", userService.loadUserByUsername(currentUserName));
        if(currentUserName.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(currentUserName);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",currentUserName);
        return "Authentication/editPassword";
    }
    @PostMapping("/ChangeProfile")
    public String editData(Model model,@RequestParam(required = false) String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Userstable userstable =userService.loadUserByUsername(currentUserName);
        userstable.setEmail(email);
        userService.postUser(userstable);
        //model.addAttribute("account", userService.loadUserByUsername(currentUserName));
        return "redirect:/account";
    }

    @GetMapping("/EditData")
    public String editData(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        model.addAttribute("account", userService.loadUserByUsername(currentUserName));
        if(currentUserName.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(currentUserName);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",currentUserName);
        return "Authentication/editData";
    }



    @PostMapping("/ChangePassword")
    public String editPassword(Model model,@RequestParam String newPassword,@RequestParam String oldPassword,@RequestParam String repPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Userstable userstable =userService.loadUserByUsername(currentUserName);
        userstable.setPassword(newPassword);
        userService.postUser(userstable);
        //model.addAttribute("account", userService.loadUserByUsername(currentUserName));
        return "redirect:/account";
    }

}
