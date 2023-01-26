package com.example.CulinaryGuide.controllers;

import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.IngredientCategory;
import com.example.CulinaryGuide.service.IngredientCategoryService;
import com.example.CulinaryGuide.service.IngredientService;

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
@RequestMapping("/ingredientCategory")
public class IngredientCategoryController {


    private final IngredientCategoryService ingredientCategoryService;
    private final IngredientService ingredientService;
    private final RoleService roleService;

    public IngredientCategoryController(IngredientCategoryService ingredientCategoryService, IngredientService ingredientService, RoleService roleService) {
        this.ingredientCategoryService = ingredientCategoryService;
        this.ingredientService = ingredientService;
        this.roleService = roleService;
    }

    @GetMapping
    public String ingredientCategoriesView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            //model.addAttribute("dishCategories", dishCategoryService.list());
            model.addAttribute("role","anonymousUser");
            model.addAttribute("user","anonymousUser");
        }
        else {
            //model.addAttribute("dishCategories", dishCategoryService.list());
            Role role=roleService.loadRoleByUsername(nameUser);
            model.addAttribute("role",role.getAuthority());
            model.addAttribute("user",nameUser);

        }
        model.addAttribute("ingredientCategories", ingredientCategoryService.list());
        return "IngredientCategory/index1";
    }



    @GetMapping("/Delete/{id}")
    public String ingredientCategoriesDeleteView(Model model,@PathVariable("id") Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredientCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/ingredientCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("error", "false");
        model.addAttribute("ingredientCategory", ingredientCategoryService.getIngredientCategoryById(id).get());
        return "IngredientCategory/delete";
    }
    @PostMapping("/Delete/{id}")
    public String ingredientCategoriesDelete(Model model,@ModelAttribute IngredientCategory ingredientCategory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredientCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/ingredientCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        if(ingredientService.checkIngredientCategory(ingredientCategory.getId())){
            model.addAttribute("error", "Данную категорию ингредиента невозможно удалить, тк она используется");
            model.addAttribute("ingredientCategory",ingredientCategoryService.getIngredientCategoryById(ingredientCategory.getId()).get());
            return "IngredientCategory/delete";
        }
        ingredientCategoryService.deleteIngredientCategory(ingredientCategory.getId());

        return "redirect:/ingredientCategory";

    }



        @GetMapping("/Create")
        public String ingredientCategoriesAddView(Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nameUser=authentication.getName();
            if(nameUser.equals("anonymousUser")){
                return "redirect:/ingredientCategory";
            }
            Role role=roleService.loadRoleByUsername(nameUser);
            if(!role.getAuthority().equals("ADMIN")){
                return "redirect:/ingredientCategory";
            }
            model.addAttribute("role",role.getAuthority());
            model.addAttribute("user",nameUser);
            model.addAttribute("ingredientCategory",new IngredientCategory());
            return "IngredientCategory/create";
        }

    @PostMapping("/Create")
    public String ingredientCategoriesAdd(Model model,
                                          @ModelAttribute("ingredientCategory") @Valid IngredientCategory ingredientCategory,
                                          BindingResult bindingResult){

             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nameUser=authentication.getName();
            if(nameUser.equals("anonymousUser")){
                return "redirect:/ingredientCategory";
            }
            Role role=roleService.loadRoleByUsername(nameUser);
            if(!role.getAuthority().equals("ADMIN")){
                return "redirect:/ingredientCategory";
            }
            model.addAttribute("role",role.getAuthority());
            model.addAttribute("user",nameUser);

            if(ingredientCategoryService.checkNameIngredientCategory(convertToFormat(ingredientCategory.getNameIngredientCategory()))){
                bindingResult.addError(new FieldError("ingredientCategory","nameIngredientCategory", "Категория с названием "+convertToFormat(ingredientCategory.getNameIngredientCategory())+" уже существует"));
            }
            if(bindingResult.hasErrors()){
                return "IngredientCategory/create";
            }
        ingredientCategoryService.postIngredientCategory(new IngredientCategory(null,convertToFormat(ingredientCategory.getNameIngredientCategory()),null));
        return "redirect:/ingredientCategory";

    }



    private String convertToFormat(String string){
        if(!string.trim().isEmpty()){
            string=string.trim();
            string=(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());}
        return string;
    }



    @GetMapping("/Edit/{id}")
    public String ingredientCategoriesEditView(Model model,@PathVariable("id") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredientCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/ingredientCategory";
        }

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("ingredientCategory", ingredientCategoryService.getIngredientCategoryById(id).get());
        return "IngredientCategory/edit";
    }

    @PostMapping("/Edit/{id}")
    public String ingredientCategoriesEdit(Model model,
                                              @ModelAttribute("ingredientCategory") @Valid IngredientCategory ingredientCategory,
                                              BindingResult bindingResult){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredientCategory";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!role.getAuthority().equals("ADMIN")){
            return "redirect:/ingredientCategory";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        IngredientCategory oldIngredientCategory =ingredientCategoryService.getIngredientCategoryByNameIngredientCategory(ingredientCategory.getNameIngredientCategory());
        if(oldIngredientCategory!=null&&((oldIngredientCategory.getNameIngredientCategory().equals(convertToFormat(ingredientCategory.getNameIngredientCategory())))&&ingredientCategory.getId()!=oldIngredientCategory.getId())){
            bindingResult.addError(new FieldError("ingredientCategory", "nameIngredientCategory", ingredientCategory.getNameIngredientCategory(), true, null, null, "Категория с названием "+convertToFormat(ingredientCategory.getNameIngredientCategory())+" уже существует"));
        }

        if(ingredientCategoryService.getIngredientCategoryById(ingredientCategory.getId()).get().getIngredients().size()>0){
            bindingResult.addError(new FieldError("ingredientCategory", "nameIngredientCategory", ingredientCategory.getNameIngredientCategory(), true, null, null, "Данную категорию невозможно изменить, тк она содержит ингредиенты"));
        }
        if(bindingResult.hasErrors()){
            return "IngredientCategory/edit";
        }
        ingredientCategoryService.putIngredientCategory(new IngredientCategory(ingredientCategory.getId(),convertToFormat(ingredientCategory.getNameIngredientCategory()),null));
        return "redirect:/ingredientCategory";


    }



}
