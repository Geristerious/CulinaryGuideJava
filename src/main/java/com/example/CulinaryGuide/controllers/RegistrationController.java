package com.example.CulinaryGuide.controllers;


import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.Authentication.Userstable;
import com.example.CulinaryGuide.service.RoleService;
import com.example.CulinaryGuide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    final private UserService userService;
    @Autowired
    final private RoleService roleService;

    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String currentUserName = authentication.getName();
//        if(!currentUserName.equals("anonymousUser")){
//            return "redirect:/cooking";
//        }
        //Role role=roleService.loadRoleByUsername(currentUserName);
        model.addAttribute("role","anonymousUser");
        model.addAttribute("user","anonymousUser");
        model.addAttribute("userForm", new Userstable());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid Userstable userForm,
                          BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
//        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
//            model.addAttribute("passwordError", "Пароли не совпадают");
//            return "registration";
//        }
        userForm.setEnabled(1L);
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        userService.saveUser(userForm);
        roleService.saveRole(new Role("USER",userForm.getUsername()));
        return "redirect:/cooking";
   }







}
