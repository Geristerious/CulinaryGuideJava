package com.example.CulinaryGuide.controllers;
import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.models.Dish;
import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.service.CookingService;
import com.example.CulinaryGuide.service.DishCategoryService;
import com.example.CulinaryGuide.service.DishService;
import com.example.CulinaryGuide.service.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;
    private  final DishCategoryService dishCategoryService;
    private final CookingService cookingService;
    private final RoleService roleService;

    public DishController(DishService dishService, DishCategoryService dishCategoryService, CookingService cookingService, RoleService roleService) {
        this.dishService = dishService;
        this.dishCategoryService = dishCategoryService;
        this.cookingService = cookingService;
        this.roleService = roleService;
    }
    @GetMapping
    public String dishesView(Model model) {
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
        model.addAttribute("dishCategories",dishCategoryService.list());
        model.addAttribute("selectedDishCategory", "");

        if(nameUser.equals("anonymousUser")){
            model.addAttribute("dishes", dishService.list());
        }
        else {
            List<Dish> myDishes=dishService.getDishByAuthor(nameUser);
            List<Dish> otherDishes=new ArrayList<>();
            for (Dish dish: dishService.list()) {
                if(!dish.getAuthor().equals(nameUser)){
                    otherDishes.add(dish);
                }
            }
            List<Dish> resultList=new ArrayList<>();
            resultList.addAll(myDishes);
            resultList.addAll(otherDishes);
            model.addAttribute("dishes", resultList);
        }
       // model.addAttribute("dishes", dishService.list());
        return "Dish/index";
    }

    @PostMapping
    public String dishesView(Model model, @RequestParam String selectedDishCategoryId) {
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
        model.addAttribute("dishCategories",dishCategoryService.list());
        model.addAttribute("selectedDishCategory", "");

        if(!selectedDishCategoryId.equals("")){

            model.addAttribute("selectedDishCategory", dishCategoryService.getDishCategoryById(Long.parseLong(selectedDishCategoryId)).get().getNameDishCategory());

            List<Dish> dishes=dishCategoryService.getDishCategoryById(Long.parseLong(selectedDishCategoryId)).get().getDishes().stream().toList();


            if(nameUser.equals("anonymousUser")){
                model.addAttribute("dishes", dishCategoryService.getDishCategoryById(Long.parseLong(selectedDishCategoryId)).get().getDishes().stream().toList());
            }
            else {
                List<Dish> myDishes=new ArrayList<>();
                for (Dish dish: dishes) {
                    if(dish.getAuthor().equals(nameUser)){
                        myDishes.add(dish);
                    }
                }

                List<Dish> otherDishes=new ArrayList<>();
                for (Dish dish: dishes) {
                    if(!dish.getAuthor().equals(nameUser)){
                        otherDishes.add(dish);
                    }
                }
                List<Dish> resultList=new ArrayList<>();
                resultList.addAll(myDishes);
                resultList.addAll(otherDishes);
                model.addAttribute("dishes", resultList);
            }

            //model.addAttribute("dishes", dishCategoryService.getDishCategoryById(Long.parseLong(selectedDishCategoryId)).get().getDishes().stream().toList());

        }else {
            model.addAttribute("selectedDishCategory", "");
            if(nameUser.equals("anonymousUser")){
                model.addAttribute("dishes", dishService.list());
            }
            else {
                List<Dish> myDishes=dishService.getDishByAuthor(nameUser);
                List<Dish> otherDishes=new ArrayList<>();
                for (Dish dish: dishService.list()) {
                    if(!dish.getAuthor().equals(nameUser)){
                        otherDishes.add(dish);
                    }
                }
                List<Dish> resultList=new ArrayList<>();
                resultList.addAll(myDishes);
                resultList.addAll(otherDishes);
                model.addAttribute("dishes", resultList);
            }
        }
        return "Dish/index";
    }




    @GetMapping("/ViewDish/{id}")
    public String dishesView(Model model, @PathVariable("id") Long selectedDishCategoryId) {
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
        model.addAttribute("dishCategories",dishCategoryService.list());
        model.addAttribute("selectedDishCategory", "");

        if(!selectedDishCategoryId.equals("")){

            model.addAttribute("selectedDishCategory", dishCategoryService.getDishCategoryById((selectedDishCategoryId)).get().getNameDishCategory());

            List<Dish> dishes=dishCategoryService.getDishCategoryById((selectedDishCategoryId)).get().getDishes().stream().toList();


            if(nameUser.equals("anonymousUser")){
                model.addAttribute("dishes", dishCategoryService.getDishCategoryById((selectedDishCategoryId)).get().getDishes().stream().toList());
            }
            else {
                List<Dish> myDishes=new ArrayList<>();
                for (Dish dish: dishes) {
                    if(dish.getAuthor().equals(nameUser)){
                        myDishes.add(dish);
                    }
                }

                List<Dish> otherDishes=new ArrayList<>();
                for (Dish dish: dishes) {
                    if(!dish.getAuthor().equals(nameUser)){
                        otherDishes.add(dish);
                    }
                }
                List<Dish> resultList=new ArrayList<>();
                resultList.addAll(myDishes);
                resultList.addAll(otherDishes);
                model.addAttribute("dishes", resultList);
            }

            //model.addAttribute("dishes", dishCategoryService.getDishCategoryById(Long.parseLong(selectedDishCategoryId)).get().getDishes().stream().toList());

        }else {
            model.addAttribute("selectedDishCategory", "");
            if(nameUser.equals("anonymousUser")){
                model.addAttribute("dishes", dishService.list());
            }
            else {
                List<Dish> myDishes=dishService.getDishByAuthor(nameUser);
                List<Dish> otherDishes=new ArrayList<>();
                for (Dish dish: dishService.list()) {
                    if(!dish.getAuthor().equals(nameUser)){
                        otherDishes.add(dish);
                    }
                }
                List<Dish> resultList=new ArrayList<>();
                resultList.addAll(myDishes);
                resultList.addAll(otherDishes);
                model.addAttribute("dishes", resultList);
            }
        }
        return "Dish/index";
    }




    @GetMapping("/Delete/{id}")
    public String dishDeleteView(Model model, @PathVariable("id") Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dish";
        }
        Role role=roleService.loadRoleByUsername(nameUser);

        if(!dishService.getDishById(id).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/dish";
        }

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("error", "false");
        model.addAttribute("dish", dishService.getDishById(id).get());
        return "Dish/delete";
    }




    @PostMapping("/Delete/{id}")
    public String dishDelete(Model model,@ModelAttribute Dish dish) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dish";
        }
        Role role=roleService.loadRoleByUsername(nameUser);



        if(!dishService.getDishById(dish.getId()).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/dish";
        }

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        if(cookingService.checkCooking(dish.getId())){
            model.addAttribute("dish", dishService.getDishById(dish.getId()).get());
            model.addAttribute("error", "Данное блюдо невозможно удалить, тк оно содержит рецепты");
            return "dish/delete";
        }
        dishService.deleteDish(dish.getId());
        return "redirect:/dish";
    }





    @GetMapping("/Create")
    public String dishAddView(Model model, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dish";
        }
        Role role=roleService.loadRoleByUsername(nameUser);

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("dish",new Dish());
        model.addAttribute("allDishCategories",dishCategoryService.list());
        model.addAttribute("selectedDishCategories",new ArrayList<Long>());
        model.addAttribute("path","redirect:" + request.getHeader("referer"));
        return "Dish/create";
    }

    @PostMapping("/Create")
    public String dishAdd(Model model, @ModelAttribute("dish") @Valid Dish dish,
                                BindingResult bindingResult,
                          @RequestParam(required=false) Collection<Long> selectedDishCategoriesId,
                          @RequestParam String path){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dish";
        }
        Role role=roleService.loadRoleByUsername(nameUser);

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("path",path);


        if(dishService.checkNameDish(convertToFormat(dish.getNameDish()))){
            bindingResult.addError(new FieldError("dish","nameDish", "Блюдо с названием "+convertToFormat(dish.getNameDish())+" уже существует"));
        }
        if(selectedDishCategoriesId==null){
            bindingResult.addError(new FieldError("dish","dishCategories", "Выберите категорию блюда"));
            model.addAttribute("selectedDishCategories",new ArrayList<Long>());
        }
        else{
            model.addAttribute("selectedDishCategories",selectedDishCategoriesId);
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("allDishCategories",dishCategoryService.list());
            return "Dish/create";
        }
        Dish newDish=new Dish();
        newDish.setNameDish(convertToFormat(dish.getNameDish()));
        if(dish.getValueCalories().equals("")){
            newDish.setValueCalories(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueCalories(dish.getValueCalories());
        }

        if(dish.getValueCarbohydrates().equals("")){
            newDish.setValueCarbohydrates(BigDecimal.valueOf(1));
        }
        else{
         newDish.setValueCarbohydrates(dish.getValueCarbohydrates());
        }

        if(dish.getValueFats().equals("")){
            newDish.setValueFats(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueFats(dish.getValueFats());
        }
        if(dish.getValueProteins().equals("")){
            newDish.setValueProteins(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueProteins(dish.getValueProteins());
        }

        newDish.setAuthor(nameUser);
        newDish.getDishCategories()
                .addAll(
                        selectedDishCategoriesId.stream().map(x->{
                            DishCategory dishCategory=dishCategoryService.getDishCategoryById(x).get();
                            dishCategory.getDishes().add(newDish);
                            return  dishCategory;
                        }).toList()
                );
        dishService.postDish(newDish);
        if(path==null){
            return "redirect:/dish";
        }
        else {
            return path;
        }
    }






    private String convertToFormat(String string){
        if(!string.trim().isEmpty()){
            string=string.trim();
            string=(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());}
        return string;
    }

    //---------
    @GetMapping("/Edit/{id}")
    public String dishEditView(Model model,@PathVariable("id") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dish";
        }
        Role role=roleService.loadRoleByUsername(nameUser);

        if(!dishService.getDishById(id).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/dish";
        }

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        model.addAttribute("dish",dishService.getDishById(id).get());
        model.addAttribute("allDishCategories",dishCategoryService.list());
        List<Long> selectedDishCategories =dishService.getDishById(id).get().getDishCategories().stream().map(
                x->{
                    DishCategory dishCategory=dishCategoryService.getDishCategoryById(x.getId()).get();
                    return  dishCategory.getId();
                }
        ).toList();
        model.addAttribute("selectedDishCategories",selectedDishCategories);
        return "Dish/edit";
    }


    @PostMapping("/Edit/{id}")
    public String dishEdit(Model model, @ModelAttribute("dish") @Valid Dish dish,
                           BindingResult bindingResult,@RequestParam(required=false) Collection<Long> selectedDishCategoriesId)
    {


        if(selectedDishCategoriesId==null){
            //bindingResult.addError(new FieldError("dish","dishCategories", "Выберите категорию блюда"));
            model.addAttribute("selectedDishCategories",new ArrayList<Long>());
        }
        else{
            model.addAttribute("selectedDishCategories",selectedDishCategoriesId);
        }
        model.addAttribute("allDishCategories",dishCategoryService.list());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/dish";
        }
        Role role=roleService.loadRoleByUsername(nameUser);



        if(!dishService.getDishById(dish.getId()).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/dish";
        }

        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        //cooking cheak
        //
        //model.addAttribute("activePage", "Dish");
        //if(){
        //            model.addAttribute("dish", dishService.getDishById(dish.getId()).get());
        //            model.addAttribute("error", "Данное блюдо невозможно удалить, тк оно содержит рецепты");
        //            return "dish/delete";
        //        }
        if(cookingService.checkCooking(dish.getId())){
            bindingResult.addError(new FieldError("dish", "nameDish", dish.getNameDish(), true, null, null, "Данное блюдо невозможно изменить, тк оно содержит рецепты"));
            //return "Ingredient/edit";
        }

        Dish oldDish =dishService.getDishById(dish.getId()).get();

        if(oldDish!=null&&((oldDish.getNameDish().equals(convertToFormat(dish.getNameDish())))&&dish.getId()!=oldDish.getId())){
            bindingResult.addError(new FieldError("dish", "nameDish", dish.getNameDish(), true, null, null, "Блюдо с названием "+dish.getAuthor()+" уже существует"));
        }



        if(bindingResult.hasErrors()){

            return "Dish/edit";
        }
        //-----------------------
        Dish newDish=new Dish();
        newDish.setId(dish.getId());
        newDish.setNameDish(convertToFormat(dish.getNameDish()));
        if(dish.getValueCalories().equals("")){
            newDish.setValueCalories(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueCalories(dish.getValueCalories());
        }

        if(dish.getValueCarbohydrates().equals("")){
            newDish.setValueCarbohydrates(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueCarbohydrates(dish.getValueCarbohydrates());
        }

        if(dish.getValueFats().equals("")){
            newDish.setValueFats(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueFats(dish.getValueFats());
        }
        if(dish.getValueProteins().equals("")){
            newDish.setValueProteins(BigDecimal.valueOf(1));
        }
        else{
            newDish.setValueProteins(dish.getValueProteins());
        }

        newDish.setAuthor(dish.getAuthor());

        newDish.getDishCategories()
                .addAll(
                        selectedDishCategoriesId.stream().map(x->{
                            DishCategory dishCategory=dishCategoryService.getDishCategoryById(x).get();
                            dishCategory.getDishes().add(newDish);
                            return  dishCategory;
                        }).toList()
                );
        dishService.postDish(newDish);
        return "redirect:/dish";
    }


}
