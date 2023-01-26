package com.example.CulinaryGuide.controllers;

import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.IngredientCategory;
import com.example.CulinaryGuide.service.DishCategoryService;

import com.example.CulinaryGuide.service.DishService;
import com.example.CulinaryGuide.service.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/dishCategory")
public class DishCategoryController {

    private final DishCategoryService dishCategoryService;
    private final RoleService roleService;
    private final DishService dishService;


    public DishCategoryController(DishCategoryService dishCategoryService, RoleService roleService, DishService dishService) {
        this.dishCategoryService = dishCategoryService;
        this.roleService = roleService;
        this.dishService = dishService;
    }

    @GetMapping
    public String dishCategoriesView(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            model.addAttribute("dishCategories", dishCategoryService.list());
            model.addAttribute("role","anonymousUser");
            model.addAttribute("user","anonymousUser");
        }
        else {
            model.addAttribute("dishCategories", dishCategoryService.list());
            Role role=roleService.loadRoleByUsername(nameUser);
            model.addAttribute("role",role.getAuthority());
            model.addAttribute("user",nameUser);

        }
        return "DishCategory/index";
    }

    @GetMapping("/Delete/{id}")
    public String dishCategoriesDeleteView(Model model,@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dishCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/dishCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("error", "false");
        model.addAttribute("dishCategory", dishCategoryService.getDishCategoryById(id).get());
        return "DishCategory/delete";
    }

    @PostMapping("/Delete/{id}")
    public String dishCategoriesDelete(Model model,@ModelAttribute DishCategory dishCategory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dishCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/dishCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        if(dishCategoryService.getDishCategoryById(dishCategory.getId()).get().getDishes().size()>0){
            model.addAttribute("error", "Данную категорию блюда невозможно удалить, тк она используется");
            model.addAttribute("dishCategory",dishCategoryService.getDishCategoryById(dishCategory.getId()).get());
            return "DishCategory/delete";
        }
        dishCategoryService.deleteDishCategory(dishCategory.getId());
        return "redirect:/dishCategory";
    }

    @GetMapping("/Create")
    public String dishCategoriesAddView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dishCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/dishCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("dishCategory",new DishCategory());
        return "DishCategory/create";
    }


    @PostMapping("/Create")
    public String  dishCategoriesAdd(Model model,
                                          @ModelAttribute("dishCategory") @Valid DishCategory dishCategory,
                                          BindingResult bindingResult){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dishCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/dishCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        if(dishCategoryService.checkNameDishCategory(convertToFormat(dishCategory.getNameDishCategory()))){
            bindingResult.addError(new FieldError("dishCategory","nameDishCategory", "Категория с названием "+convertToFormat(dishCategory.getNameDishCategory())+" уже существует"));
        }
        if(bindingResult.hasErrors()){
            return "DishCategory/create";
        }
        dishCategoryService.postDishCategory(new DishCategory(null,convertToFormat(dishCategory.getNameDishCategory()),null));
        return "redirect:/dishCategory";
    }

    private String convertToFormat(String string){
        if(!string.trim().isEmpty()){
            string=string.trim();
            string=(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());}
        return string;
    }


    @GetMapping("/Edit/{id}")
    public String dishCategoriesEditView(Model model,@PathVariable("id") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dishCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/dishCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("dishCategory", dishCategoryService.getDishCategoryById(id).get());
        return "DishCategory/edit";
    }

    @PostMapping("/Edit")
    public String dishCategoriesEdit(Model model,
                                           @ModelAttribute("dishCategory") @Valid DishCategory dishCategory,
                                           BindingResult bindingResult){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dishCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/dishCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        DishCategory oldDishCategory =dishCategoryService.getDishCategoryByNameDishCategory(convertToFormat(dishCategory.getNameDishCategory()) );
        if(oldDishCategory!=null&&((oldDishCategory.getNameDishCategory().equals(convertToFormat(dishCategory.getNameDishCategory())))&&dishCategory.getId()!=oldDishCategory.getId())){
           // bindingResult.addError(new ObjectError("nameDishCategory", "Категория с названием "+oldDishCategory.getNameDishCategory()+" уже существует"));
            bindingResult.addError(new FieldError("dishCategory", "nameDishCategory", dishCategory.getNameDishCategory(), true, null, null, "Категория с названием "+convertToFormat(dishCategory.getNameDishCategory())+" уже существует"));
        }
        if(dishCategoryService.getDishCategoryById(dishCategory.getId()).get().getDishes().size()>0){
            bindingResult.addError(new FieldError("dishCategory", "nameDishCategory", dishCategory.getNameDishCategory(), true, null, null, "Данную категорию невозможно изменить, тк она содержит блюда"));

        }
        if(bindingResult.hasErrors()){
            return "DishCategory/edit";
        }
        dishCategoryService.postDishCategory(new DishCategory(dishCategory.getId(),convertToFormat(dishCategory.getNameDishCategory()),null));
        return "redirect:/dishCategory";
    }

}
