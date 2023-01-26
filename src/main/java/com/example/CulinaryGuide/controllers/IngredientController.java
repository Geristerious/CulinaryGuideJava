package com.example.CulinaryGuide.controllers;
import com.example.CulinaryGuide.models.*;
import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.service.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/ingredient")
public class IngredientController {

    public static Cooking cookingI;
    public static Collection<String> selectedIngredientDishesI;
    public static Collection<String> selectedIngredientRI;
    private final RoleService roleService;
    private final IngredientService ingredientService;
    private final IngredientCategoryService ingredientCategoryService;
    private final IngredientDishService ingredientDishService;
    private final IngredientReplacementService ingredientReplacementService;
    public IngredientController(RoleService roleService, IngredientService ingredientService, IngredientCategoryService ingredientCategoryService, IngredientDishService ingredientDishService, IngredientReplacementService ingredientReplacementService) {
        this.roleService = roleService;
        this.ingredientService = ingredientService;
        this.ingredientCategoryService = ingredientCategoryService;
        this.ingredientDishService = ingredientDishService;
        this.ingredientReplacementService = ingredientReplacementService;
    }


    @GetMapping("/Delete/{id}")
    public String ingredientDeleteView(Model model,@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("error", "false");
        model.addAttribute("ingredient", ingredientService.getIngredientById(id).get());
        return "Ingredient/delete";
    }


    @PostMapping("/Delete/{id}")
    public String ingredientDelete(Model model,@ModelAttribute Ingredient ingredient) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        List<Long> newList=ingredientDishService.getCookingByIngredient(ingredient.getId()).stream().map(cooking->cooking.getCooking().getId()).toList();
        List<Long> newRList=ingredientReplacementService.getCookingByIngredient(ingredient.getId()).stream().map(cooking->cooking.getCooking().getId()).toList();
        if(newList.size()>0||newRList.size()>0){
            model.addAttribute("ingredient", ingredientService.getIngredientById(ingredient.getId()).get());
            model.addAttribute("error", "Данный ингредиент невозможно удалить, тк он используется в рецептах");
            return "Ingredient/delete";
        }
        ingredientService.deleteIngredient(ingredient.getId());
        return "redirect:/ingredient";
    }

    @GetMapping("/Create")
    public String ingredientAddView(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("path","redirect:/ingredient");
        model.addAttribute("ingredient",new Ingredient());
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("cooking",null);
        model.addAttribute("selectedIngredientDishes",new ArrayList<>());
        model.addAttribute("selectedIngredientReplaces",new ArrayList<>());
        return "Ingredient/create";
    }


    @PostMapping("/CreateIngredientAddFromOther")
    public String ingredientAddFromOther(Model model, HttpServletRequest request,
                                         @ModelAttribute Cooking cooking,
                                         @RequestParam(required=false) Collection<String> selectedIngredientDishesList
               ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
         model.addAttribute("ingredient",new Ingredient());
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("cooking",cooking);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        model.addAttribute("selectedIngredientReplaces",null);
        model.addAttribute("path","redirect:/cooking/AddIngredientInRecipe");
        return "Ingredient/create";
    }




    @PostMapping("/CreateIngredientAddFromOtherR")
    public String ingredientAddFromOtherR(Model model, HttpServletRequest request,
                                         @ModelAttribute Cooking cooking,
                                         @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                         @RequestParam(required=false) Collection<String> selectedIngredientReplacesList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("ingredient",new Ingredient());
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("cooking",cooking);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        model.addAttribute("selectedIngredientReplaces",selectedIngredientReplacesList);
        model.addAttribute("path","redirect:/cooking/AddIngredientRInRecipe");
        return "Ingredient/create";
    }




    @PostMapping("/CreateIngredientAddFromOtherC")
    public String ingredientAddFromOtherC(Model model, HttpServletRequest request,
                                         @ModelAttribute Cooking cooking,
                                         @RequestParam(required=false) Collection<String> selectedIngredientDishesList
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("ingredient",new Ingredient());
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("cooking",cooking);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        model.addAttribute("selectedIngredientReplaces",null);
        model.addAttribute("path","redirect:/cooking/ChangeAddIngredientInRecipe");
        return "Ingredient/create";
    }


    @PostMapping("/CreateIngredientAddFromOtherRC")
    public String ingredientAddFromOtherRC(Model model, HttpServletRequest request,
                                          @ModelAttribute Cooking cooking,
                                          @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                          @RequestParam(required=false) Collection<String> selectedIngredientReplacesList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("ingredient",new Ingredient());
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("cooking",cooking);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        model.addAttribute("selectedIngredientReplaces",selectedIngredientReplacesList);
        model.addAttribute("path","redirect:/cooking/ChangeAddIngredientRInRecipe");
        return "Ingredient/create";
    }


    @PostMapping("/Create")
    public String ingredientAdd(Model model, @ModelAttribute("ingredient") @Valid Ingredient ingredient, BindingResult bindingResult,
                                @RequestParam String path,
                                @ModelAttribute Cooking cooking,
                                @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                @RequestParam(required=false) Collection<String> selectedIngredientReplacesList,
                                @RequestParam(required=false) String selectedIngredientCategory){

        model.addAttribute("path",path);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        model.addAttribute("selectedIngredientReplaces",selectedIngredientReplacesList);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        if(ingredientService.checkNameIngredient(convertToFormat(ingredient.getNameIngredient()))){
            bindingResult.addError(new FieldError("ingredient","nameIngredient", "Ингредиент с названием "+convertToFormat(ingredient.getNameIngredient())+" уже существует"));
        }
        if(selectedIngredientCategory.equals("")){
            bindingResult.addError(new FieldError("ingredient","ingredientCategory", "Выберите категорию"));
        }

        if(bindingResult.hasErrors()){
            //ingredient.setIngredientCategory(ingredientCategoryService.getIngredientCategoryById(ingredient.getIngredientCategory().getId()).get());
            model.addAttribute("ingredientCategories",ingredientCategoryService.list());
           if(!selectedIngredientCategory.equals(""))
            model.addAttribute("selectedIngredientCategory",ingredientCategoryService.getIngredientCategoryById(Long.valueOf(selectedIngredientCategory)).get().getNameIngredientCategory());

            return "Ingredient/create";
        }

        IngredientCategory ingredientCategory=ingredientCategoryService.getIngredientCategoryById(ingredientCategoryService.getIngredientCategoryById(Long.valueOf(selectedIngredientCategory)).get().getId()).get();
        ingredientService.postIngredient(new Ingredient(null,convertToFormat(ingredient.getNameIngredient()),ingredientCategory,null,null));

        if(path.equals("redirect:/cooking/AddIngredientInRecipe")){
            cookingI =cooking;
            selectedIngredientDishesI=selectedIngredientDishesList;
        }else if(path.equals("redirect:/cooking/AddIngredientRInRecipe")){
            cookingI =cooking;
            selectedIngredientDishesI=selectedIngredientDishesList;
            selectedIngredientRI=selectedIngredientReplacesList;}
       else if(path.equals("redirect:/cooking/ChangeAddIngredientInRecipe")){
                cookingI =cooking;
                selectedIngredientDishesI=selectedIngredientDishesList;
                selectedIngredientRI=selectedIngredientReplacesList;
            }
        else if(path.equals("redirect:/cooking/ChangeAddIngredientRInRecipe")){
            cookingI =cooking;
            selectedIngredientDishesI=selectedIngredientDishesList;
            selectedIngredientRI=selectedIngredientReplacesList;
        }

        return path;
    }

    private String convertToFormat(String string){
        if(!string.trim().isEmpty()){
            string=string.trim();
            string=(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());}
        return string;
    }


    @GetMapping("/Edit/{id}")
    public String ingredientEditView(Model model,@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        Ingredient ingredient=new Ingredient(ingredientService.getIngredientById(id).get().getId(),ingredientService.getIngredientById(id).get().getNameIngredient(),null,null,null);
        model.addAttribute("ingredient",ingredient);
        model.addAttribute("selectedIngredientCategory",ingredientCategoryService.getIngredientCategoryById(ingredientService.getIngredientById(id).get().getIngredientCategory().getId()).get().getNameIngredientCategory());
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        return "Ingredient/edit";
    }


    @PostMapping("/Edit/{id}")
    public String ingredientEdit(Model model,
                                           @ModelAttribute("ingredient") @Valid Ingredient ingredient,
                                           BindingResult bindingResult,@RequestParam(required=false) String selectedIngredientCategory){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/ingredient";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("selectedIngredientCategory",ingredientCategoryService.getIngredientCategoryById(Long.valueOf(selectedIngredientCategory)).get().getNameIngredientCategory());
        List<Long> newList=ingredientDishService.getCookingByIngredient(ingredient.getId()).stream().map(cooking->cooking.getCooking().getId()).toList();
        List<Long> newRList=ingredientReplacementService.getCookingByIngredient(ingredient.getId()).stream().map(cooking->cooking.getCooking().getId()).toList();
        if(newList.size()>0||newRList.size()>0){
            bindingResult.addError(new FieldError("ingredient", "nameIngredient", ingredient.getNameIngredient(), true, null, null, "Данный ингредиент невозможно изменить, тк он используется в рецептах"));
        }

        if(selectedIngredientCategory.equals("")){
            bindingResult.addError(new FieldError("ingredient","ingredientCategory", "Выберите категорию"));
        }

        Ingredient oldIngredient =ingredientService.getIngredientByNameIngredient(ingredient.getNameIngredient());
        if(oldIngredient!=null&&((oldIngredient.getNameIngredient().equals(convertToFormat(ingredient.getNameIngredient())))&&ingredient.getId()!=oldIngredient.getId())){
            bindingResult.addError(new FieldError("ingredient", "nameIngredient", ingredient.getNameIngredient(), true, null, null, "Категория с названием "+ingredient.getNameIngredient()+" уже существует"));
        }

        if(bindingResult.hasErrors()){
            return "Ingredient/edit";
        }
        IngredientCategory ingredientCategory=ingredientCategoryService.getIngredientCategoryById(ingredientCategoryService.getIngredientCategoryById(Long.valueOf(selectedIngredientCategory)).get().getId()).get();
        ingredientService.postIngredient(new Ingredient(ingredient.getId(),convertToFormat(ingredient.getNameIngredient()),ingredientCategory,null,null));
        //ingredientService.postIngredient(new Ingredient(,convertToFormat(ingredient.getNameIngredient()),ingredient.getIngredientCategory(),ingredient.getIngredientDishes(),ingredient.getIngredientReplacements()));
        return "redirect:/ingredient";
    }

    @GetMapping
    public String ingredientsView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            model.addAttribute("role","anonymousUser");
        }
        else {
            Role role=roleService.loadRoleByUsername(nameUser);
            model.addAttribute("role",role.getAuthority());
        }
        //Role role=roleService.loadRoleByUsername(nameUser);

        model.addAttribute("user",nameUser);
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());
        model.addAttribute("selectedIngredientCategory", "");
        model.addAttribute("ingredients", ingredientService.list());
        return "Ingredient/index";
    }
    //----------------------
    @PostMapping
    public String ingredientsView(Model model, @RequestParam String selectedIngredientCategoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            model.addAttribute("role","anonymousUser");
        }
        else {
            Role role=roleService.loadRoleByUsername(nameUser);
            model.addAttribute("role",role.getAuthority());
        }
        model.addAttribute("user",nameUser);

        model.addAttribute("ingredientCategories",ingredientCategoryService.list());

        if(!selectedIngredientCategoryId.equals("")){
            model.addAttribute("selectedIngredientCategory", ingredientCategoryService.getIngredientCategoryById(Long.parseLong(selectedIngredientCategoryId)).get().getNameIngredientCategory());
            model.addAttribute("ingredients", ingredientService.getIngredientByIngredientCategory_Id(Long.parseLong(selectedIngredientCategoryId)));
        }else {
            model.addAttribute("selectedIngredientCategory", "");
            model.addAttribute("ingredients", ingredientService.list());
        }
        return "Ingredient/index";
    }

    @GetMapping("/ViewIngredient/{id}")
    public String ingredientsView(Model model, @PathVariable("id") Long selectedIngredientCategoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            model.addAttribute("role","anonymousUser");
        }
        else {
            Role role=roleService.loadRoleByUsername(nameUser);
            model.addAttribute("role",role.getAuthority());
        }
        model.addAttribute("user",nameUser);
        model.addAttribute("ingredientCategories",ingredientCategoryService.list());

        if(!selectedIngredientCategoryId.equals("")){
            model.addAttribute("selectedIngredientCategory", ingredientCategoryService.getIngredientCategoryById((selectedIngredientCategoryId)).get().getNameIngredientCategory());
            model.addAttribute("ingredients", ingredientService.getIngredientByIngredientCategory_Id((selectedIngredientCategoryId)));
        }else {
            model.addAttribute("selectedIngredientCategory", "");
            model.addAttribute("ingredients", ingredientService.list());
        }
        return "Ingredient/index";
    }

}
