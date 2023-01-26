package com.example.CulinaryGuide.controllers;

import com.example.CulinaryGuide.models.*;
import com.example.CulinaryGuide.models.Authentication.Role;
import com.example.CulinaryGuide.service.*;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.CulinaryGuide.controllers.IngredientController.*;

@Controller
@RequestMapping("/cooking")
public class CookingController {

    private IngredientController ingredientController;
    private final CookingService cookingService;
    private final DishService dishService;
    private final IngredientService ingredientService;
    private final IngredientDishService ingredientDishService;
    private final IngredientReplacementService ingredientReplacementService;
    private final CookingPhotoService cookingPhotoService;
    private final SelectedRecipesService selectedRecipesService;
    private  final RoleService roleService;


    public CookingController(IngredientController ingredientController, CookingService cookingService, DishService dishService, IngredientService ingredientService, IngredientDishService ingredientDishService, IngredientReplacementService ingredientReplacementService, CookingPhotoService cookingPhotoService, SelectedRecipesService selectedRecipesService, RoleService roleService) {
        this.ingredientController = ingredientController;
        this.cookingService = cookingService;
        this.dishService = dishService;
        this.ingredientService = ingredientService;
        this.ingredientDishService = ingredientDishService;
        this.ingredientReplacementService = ingredientReplacementService;
        this.cookingPhotoService = cookingPhotoService;
        this.selectedRecipesService = selectedRecipesService;
        this.roleService = roleService;
    }

    //ViewIngredient



    @GetMapping("/WorkWithImage/{id}")
    public String workWithImage(Model model, @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!cookingService.getCookingById(id).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/cooking";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("cooking", cookingService.getCookingById(id).get());
        return "Cooking/workWithImage";
    }

    @PostMapping("/EditAddPhoto/{id}")
       public String editAddPhoto(Model model,@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!cookingService.getCookingById(id).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/cooking";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("cooking", cookingService.getCookingById(id).get());
        return "Cooking/editAddPhoto";
    }

    @PostMapping("/SaveEditAddPhoto")
    public String saveEditAddPhoto(Model model,@ModelAttribute Cooking cooking, @RequestParam("file") List<MultipartFile> file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!cookingService.getCookingById(cooking.getId()).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/cooking";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        String path= System.getProperty("user.dir")+"\\src\\main\\resources\\static\\Images";
        Cooking newCooking=cookingService.getCookingById(cooking.getId()).get();
        if (file != null){
            for(MultipartFile image:file){
                if (file != null && !image.getOriginalFilename().isEmpty()) {
                    File uploadDir = new File(path);
//                    if (!uploadDir.exists()) {
//                        uploadDir.mkdir();
//                    }
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile  + image.getOriginalFilename();
                    image.transferTo(new File(path + "\\" + resultFilename));
                    cookingPhotoService.postCookingPhoto(new CookingPhoto(null,newCooking, resultFilename));

                }
            }
        }
        return "redirect:/cooking";
    }


   static List<CookingPhoto> imageForDelete=new ArrayList<>();


    @PostMapping("/EditDeletePhoto/{id}")
    public String editDeletePhoto(Model model,@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!cookingService.getCookingById(id).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/cooking";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("cooking", cookingService.getCookingById(id).get());
        imageForDelete=cookingService.getCookingById(id).get().getCookingPhotos();
        imageString=new ArrayList<>();
        model.addAttribute("images", imageForDelete);
        return "Cooking/editDeletePhoto";
    }


    @PostMapping("/DeleteImage")
    public String deleteImage(Model model,@ModelAttribute Cooking cooking,  @RequestParam(required=false) String filename) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        if(!cookingService.getCookingById(cooking.getId()).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
            return "redirect:/cooking";
        }
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);
        model.addAttribute("cooking", cookingService.getCookingById(cooking.getId()).get());
        imageForDelete.removeIf(x->x.getFilename().equals(filename));
        imageString.add(filename);
        //imageForDelete=cookingService.getCookingById(cooking.getId()).get().getCookingPhotos();
        model.addAttribute("images", imageForDelete);
        return "Cooking/editDeletePhoto";
    }
    //static List<CookingPhoto> imageForDelete=new ArrayList<>();
    static List<String> imageString=new ArrayList<>();
    @PostMapping("/SaveEditDeletePhoto")
    public String saveEditDeletePhoto(Model model,@ModelAttribute Cooking cooking,  @RequestParam(required=false) String filename) throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String nameUser=authentication.getName();
//        if(nameUser.equals("anonymousUser")){
//            return "redirect:/cooking";
//        }
//        Role role=roleService.loadRoleByUsername(nameUser);
//        if(!cookingService.getCookingById(cooking.getId()).get().getAuthor().equals(nameUser)&&!role.getAuthority().equals("ADMIN")){
//            return "redirect:/cooking";
//        }
//        model.addAttribute("role",role.getAuthority());
//        model.addAttribute("user",nameUser);
       if(imageString!=null){
           for(String image:imageString){
               String path= System.getProperty("user.dir")+"\\src\\main\\resources\\static\\Images";
               Files.delete(Path.of(path + "\\" + image));
           }
           for (var item :cookingPhotoService.list() ){
               if(imageString.contains(item.getFilename())){
                   cookingPhotoService.deleteCookingPhoto(item.getId());
               }
           }
           //.deleteCookingPhoto(.stream().map(x->(x.getCooking().getId()==cooking.getId()&&(x.getFilename().equals(image)))).count()


       }


         //image.transferTo(new File(  resultFilename));
        imageString=new ArrayList<>();
        imageForDelete=new ArrayList<>();
        return "redirect:/cooking";
    }

    @GetMapping("/SelectedRecipe")
    public String selectedRecipeView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

         List<Cooking> selectedRecipesList=new ArrayList<>();
         for (SelectedRecipes selectedRecipes: selectedRecipesService.list()){
             if(selectedRecipes.getAuthor().equals(nameUser))
             selectedRecipesList.add(selectedRecipes.getCooking());
         }
        //selectedRecipesService.getSelectedRecipesByAuthor(currentUserName);
         model.addAttribute("cookings", selectedRecipesList);
         return "Cooking/selectedRecipe";
    }

    @GetMapping("/DeleteSelectedRecipes/{id}")
    public String deleteSelectedRecipes(Model model, @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nameUser=authentication.getName();
        if(nameUser.equals("anonymousUser")){
            return "redirect:/cooking";
        }
        Role role=roleService.loadRoleByUsername(nameUser);
        model.addAttribute("role",role.getAuthority());
        model.addAttribute("user",nameUser);

        for(SelectedRecipes selectedRecipe:selectedRecipesService.list()){
            if(selectedRecipe.getCooking().getId()==id&&selectedRecipe.getAuthor().equals(nameUser)){
                selectedRecipesService.deleteSelectedRecipe(selectedRecipe.getId());
            }
        }

        List<Cooking> selectedRecipesList=new ArrayList<>();
        for (SelectedRecipes selectedRecipes: selectedRecipesService.list()){
            if(selectedRecipes.getAuthor().equals(nameUser))
                selectedRecipesList.add(selectedRecipes.getCooking());
        }
        model.addAttribute("cookings",  selectedRecipesList);
        return "redirect:/cooking/SelectedRecipe";
    }
    @GetMapping("/AddSelectedRecipes/{id}")
    public String addSelectedRecipes(Model model, @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        selectedRecipesService.postSelectedRecipe(new SelectedRecipes(null,currentUserName,cookingService.getCookingById(id).get()));
        return "redirect:/cooking";
    }
    //----------



    @GetMapping

    public String cookingView(Model model) {

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
        model.addAttribute("dishes",dishService.list());
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("selectedDish", "");
        model.addAttribute("selectedIngredients", new ArrayList<Long>());
        model.addAttribute("cookings", cookingService.list());
        return "Cooking/index";
    }



    @PostMapping
    public String cookingView(Model model, @RequestParam String selectedDishId,
                              @RequestParam(required=false) ArrayList<Long> selectedIngredientsId) {


        List<Long> resultDishList=new ArrayList<>();
        List<Long> resulCookingList=new ArrayList<>();
        //List<Long>resultCookingList=new ArrayList<>();
        model.addAttribute("cookings", cookingService.list());
        if(selectedDishId.equals("")){
            model.addAttribute("selectedDish", "");
        }
        else{
            model.addAttribute("selectedDish",   dishService.getDishById(Long.valueOf(selectedDishId)).get().getNameDish());

        }

        if(selectedIngredientsId==null){
            model.addAttribute("selectedIngredients", new ArrayList<Long>());
        }
        else {
            model.addAttribute("selectedIngredients", selectedIngredientsId);
        }

        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("dishes",dishService.list());
        model.addAttribute("role","admin");
        if(selectedDishId.equals("")&&(selectedDishId==null)){
            model.addAttribute("cookings", cookingService.list());
            return "Cooking/index";
        }

        if(!selectedDishId.equals("")){ //на null
            resultDishList.addAll(cookingService.getCookingByDishId(Long.valueOf(selectedDishId)).stream().map(cooking -> cooking.getId()).toList());
            model.addAttribute("cookings", getCookingList(resultDishList));
        }


        if(selectedIngredientsId!=null){
            List<List<Long>> cookingIdList=new ArrayList<List<Long>>();
            for(Long id: selectedIngredientsId){

                List<Long> newList=ingredientDishService.getCookingByIngredient(id).stream().map(cooking->cooking.getCooking().getId()).toList();
                List<Long> newRList=ingredientReplacementService.getCookingByIngredient(id).stream().map(cooking->cooking.getCooking().getId()).toList();


                if(newList.size()!=0&&newRList.size()!=0){
                    List<Long> unification = Stream.concat(ingredientDishService.getCookingByIngredient(id).stream().map(cooking->cooking.getCooking().getId()).toList().stream(),
                                    ingredientReplacementService.getCookingByIngredient(id).stream().map(cooking->cooking.getCooking().getId()).toList().stream())
                            .collect(Collectors.groupingBy(emp -> emp,
                                    Collectors.reducing(null, (e1, e2) -> e1 ) ))
                            .values().stream().toList();

                    cookingIdList.add(unification);
                }

                if(newList.size()!=0){
                    cookingIdList.add(newList);
                }
                else if(newRList.size()!=0){
                    cookingIdList.add(newRList);
                }

            }

            resulCookingList=FillUniteList(cookingIdList, true);

            model.addAttribute("cookings", getCookingList(resulCookingList));
        }


        if(!selectedDishId.equals("")&&selectedIngredientsId!=null){
            List<Long> resultList = Stream.concat(resulCookingList.stream(),
                            resultDishList.stream())
                    .collect(Collectors.groupingBy(emp -> emp,
                            Collectors.reducing(null, (e1, e2) -> e1 ) ))
                    .values().stream().toList();
            model.addAttribute("cookings", getCookingList(resultList));

        }
        return "Cooking/index";
    }


    private List<Cooking> getCookingList(List<Long> list){
        List<Cooking> cookingList=new ArrayList<>();
        for (Long id: list){
            cookingList.add(cookingService.getCookingById(id).get());
        }
        return cookingList;
    }





    public List<Long> FillUniteList(List<List<Long>>unityList,boolean ready){
        List<Long> result=new ArrayList<Long>();
        for (List<Long> list:unityList){
            if(ready){
                result = list;
                ready = !ready;
            }
            else {
                result =intersection(result,list);
            }
        }
        return result;
    }

    public List<Long> intersection(List<Long> list1, List<Long> list2) {
        List<Long> list = new ArrayList<>();
        for (Long id : list1) {
            if(list2.contains( id)) {
                list.add( id);
            }
        }
        return list;
    }

//    public ArrayList<Integer> searchIngredient(String query) throws SQLException {
//        ArrayList<Integer> list=new ArrayList<Integer>();
//        ResultSet resultSet = statement.executeQuery(query);
//        while (resultSet.next()){
//            list.add(resultSet.getInt("id_Dish"));
//        }
//        return  list;
//    }




    //-----------------------------------------------------
    @GetMapping("/Delete/{id}")
    public String cookingDeleteView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("activePage", "Cooking");
        model.addAttribute("errorMessage", "noError");
        model.addAttribute("cooking", cookingService.getCookingById(id).get());
        return "Cooking/delete";
    }

    @PostMapping("/Delete/{id}")
    public String cookingDelete(Model model,@ModelAttribute Cooking cooking) {
        cookingService.deleteCooking(cooking.getId());
        return "redirect:/cooking";
    }

    //----------------------------------------------------------------------------------------------------------------

    @GetMapping("/ChoiceDish")
    public String cookingAddView(Model model, HttpServletRequest request) {
        model.addAttribute("activePage", "Cooking");
        model.addAttribute("dishes",dishService.list());
        return "Cooking/choiceDish";
    }


    @GetMapping("/CreateCooking/{id}")
    public String choiceDishCookingAddFromOther(Model model, @PathVariable("id") Long dishId){
        model.addAttribute("dishId",dishId);
        Cooking cooking=new Cooking();
        cooking.setTimeCooking("00:00");
        Dish dish=dishService.getDishById(dishId).get();
        cooking.setDish(new Dish(dish.getId(),dish.getNameDish()));
        model.addAttribute("cooking",cooking);
        return "Cooking/createCooking";
    }


    @PostMapping("/CreateCooking")
    public String choiceDishCookingAdd(Model model, @RequestParam Long dishId){
        model.addAttribute("dishId",dishId);
        Cooking cooking=new Cooking();
        cooking.setTimeCooking("00:00");
        Dish dish=dishService.getDishById(dishId).get();
        cooking.setDish(new Dish(dish.getId(),dish.getNameDish()));
        model.addAttribute("cooking",cooking);
        return "Cooking/createCooking";
    }

    @PostMapping("/AddIngredient")
    public String createCookingAdd(Model model, @ModelAttribute Cooking cooking){
        model.addAttribute("cooking",cooking);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientDish",new IngredientDish());
        model.addAttribute("selectedIngredientDishes",new ArrayList<String>());
        return "Cooking/addIngredient";
    }



    @GetMapping("/AddIngredientInRecipe")
    public String createCookingAddIngredientAdd(Model model){
        model.addAttribute("cooking",cookingI);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientDish",new IngredientDish());

        List<IngredientDish> ingredientDishList=new ArrayList<>();
        List<Ingredient> ingredients=ingredientService.list();
        if(selectedIngredientDishesI!=null){
            for (var item:selectedIngredientDishesI) {
                String[] data=item.split("/");
                //if(!ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)){
                    ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                //}
            }
        }
        model.addAttribute("ingredients",ingredients);
        model.addAttribute("selectedIngredientDishes",ingredientDishList);
        return "Cooking/addIngredient";
    }


    @PostMapping("/AddIngredientInRecipe")

    public String createCookingAddIngredientAdd(Model model, @ModelAttribute Cooking cooking,
                                                @ModelAttribute @Valid IngredientDish ingredientDish,
                                                @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                @RequestParam(required=false) String action){//userName
        model.addAttribute("cooking",cooking);
        model.addAttribute("ingredientDish",new IngredientDish());
        if(action.equals("Добавить ингредиент")){
            List<IngredientDish> ingredientDishList=new ArrayList<>();
            List<Ingredient> ingredients=ingredientService.list();
            if(selectedIngredientDishesList!=null){
                for (var item:selectedIngredientDishesList) {
                    String[] data=item.split("/");
                    ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                }
                ingredientDishList.add(ingredientDish);
                ingredients.removeIf(x->x.getNameIngredient().equals(ingredientDish.getIngredient().getNameIngredient()));
            }
            else{
                    ingredientDishList.add(ingredientDish);
                    ingredients.removeIf(x->x.getNameIngredient().equals(ingredientDish.getIngredient().getNameIngredient()));
            }
            model.addAttribute("ingredients",ingredients);
            model.addAttribute("selectedIngredientDishes",ingredientDishList);
            return "Cooking/addIngredient";
        }

        return "Cooking/addIngredient";
    }


    @PostMapping("/DeleteIngredientInRecipe")
    public String deleteCookingAddIngredientAdd(Model model, @ModelAttribute Cooking cooking,
                                                @RequestParam String nameIngredient,
                                                @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                @RequestParam(required=false) String action){
        model.addAttribute("cooking",cooking);
        model.addAttribute("ingredientDish",new IngredientDish());

        List<IngredientDish> ingredientDishList=new ArrayList<>();
        List<Ingredient> ingredients=ingredientService.list();
        if(selectedIngredientDishesList!=null){
            for (var item:selectedIngredientDishesList) {
                String[] data=item.split("/");
                if(!ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)){
                    ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                }
            }
        }
        model.addAttribute("ingredients",ingredients);
        model.addAttribute("selectedIngredientDishes",ingredientDishList);
        return "Cooking/addIngredient";

    }


    @PostMapping("/AddIngredientReplace")
    public String createCookingAddIngredientAddReplace(Model model,
                                                       @ModelAttribute Cooking cooking,
                                                       @RequestParam(required=false) Collection<String> selectedIngredientDishesList){
        model.addAttribute("cooking",cooking);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);

        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(selectedIngredientDishesList!=null){
            for (var item:selectedIngredientDishesList) {
                String[] data=item.split("/");
                    //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());
                    //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
            }
        }

        model.addAttribute("selectedIngredientReplaces",new ArrayList<String>());
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientsR",ingredientsForReplace);
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        return "Cooking/addIngredientR";
    }

    @GetMapping("/AddIngredientRInRecipe")
    public String createCookingAddIngredientRAdd(Model model){
        model.addAttribute("cooking",cookingI);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesI);

        List<IngredientReplacement> ingredientReplacementListList=new ArrayList<>();
        if(selectedIngredientRI!=null){
            for (var item:selectedIngredientRI) {
                String[] data=item.split("/");
                ingredientReplacementListList.add(new IngredientReplacement(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),ingredientService.getIngredientById(Long.valueOf(data[2])).get() ,data[3]));
            }
            //ingredientReplacementListList.add(ingredientReplacement);
        }

        model.addAttribute("selectedIngredientReplaces",ingredientReplacementListList);

        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(selectedIngredientDishesI!=null){
            for (var item:selectedIngredientDishesI) {
                String[] data=item.split("/");
                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());
                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
            }
        }
        model.addAttribute("ingredientsR",ingredientsForReplace);
        return "Cooking/addIngredientR";
    }


    @PostMapping("/AddIngredientRInRecipe")

    public String createCookingAddIngredientRAdd(Model model, @ModelAttribute Cooking cooking,
                                                @ModelAttribute @Valid IngredientReplacement ingredientReplacement,
                                                @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                @RequestParam(required=false) Collection<String> selectedIngredientReplacesList,
                                                @RequestParam(required=false) String action){//userName

        model.addAttribute("cooking",cooking);
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);

            List<IngredientReplacement> ingredientReplacementListList=new ArrayList<>();

            if(selectedIngredientReplacesList!=null){
                for (var item:selectedIngredientReplacesList) {
                    String[] data=item.split("/");
                    ingredientReplacementListList.add(new IngredientReplacement(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),ingredientService.getIngredientById(Long.valueOf(data[2])).get() ,data[3]));
                }
                ingredientReplacementListList.add(ingredientReplacement);
            }
            else{
                ingredientReplacementListList.add(ingredientReplacement);

            }

            List<Ingredient> ingredientsForReplace=new ArrayList<>();
            if(selectedIngredientDishesList!=null){
                for (var item:selectedIngredientDishesList) {
                    String[] data=item.split("/");
                    //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());
                    //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                }
            }

            model.addAttribute("ingredientsR",ingredientsForReplace);
            model.addAttribute("selectedIngredientReplaces",ingredientReplacementListList);


            model.addAttribute("ingredients",ingredientService.list());

        return "Cooking/addIngredientR";
    }


    @PostMapping("/DeleteIngredientRInRecipe")
    public String deleteCookingAddIngredientRAdd(Model model, @ModelAttribute Cooking cooking,
                                                @RequestParam String nameIngredient,
                                                @RequestParam String nameIngredientR,
                                                @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                 @RequestParam(required=false) Collection<String> selectedIngredientReplacesList,
                                                @RequestParam(required=false) String action){





        List<IngredientReplacement> ingredientReplacementListList=new ArrayList<>();

        if(selectedIngredientReplacesList!=null){
            for (var item:selectedIngredientReplacesList) {
                String[] data = item.split("/");

//                String nameI=ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient();
//                String nameIR=ingredientService.getIngredientById(Long.valueOf(data[2])).get().getNameIngredient();
//                boolean I=nameI.equals(nameIngredient);
//                boolean Ir=nameIR.equals(nameIngredientR);
//                boolean test=I&&Ir;

                if (!(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)&&ingredientService.getIngredientById(Long.valueOf(data[2])).get().getNameIngredient().equals(nameIngredientR))) {
                    ingredientReplacementListList.add(new IngredientReplacement(ingredientService.getIngredientById(Long.valueOf(data[0])).get(), BigDecimal.valueOf(Float.valueOf(data[1])), ingredientService.getIngredientById(Long.valueOf(data[2])).get(), data[3]));
                }
            }
            //ingredientReplacementListList.add(ingredientReplacement);
        }

        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(selectedIngredientDishesList!=null){
            for (var item:selectedIngredientDishesList) {
                String[] data=item.split("/");
                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());
                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
            }
        }

        model.addAttribute("ingredientsR",ingredientsForReplace);
        model.addAttribute("selectedIngredientReplaces",ingredientReplacementListList);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        model.addAttribute("cooking",cooking);
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        model.addAttribute("ingredients",ingredientService.list());
        return "Cooking/addIngredientR";
    }

    @PostMapping("/AddImage")
    public String addImageCooking(Model model,  @ModelAttribute Cooking cooking,
                                  @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                  @RequestParam(required=false) Collection<String> selectedIngredientReplacesList){

        model.addAttribute("cooking",cooking);
        model.addAttribute("selectedIngredientReplaces",selectedIngredientReplacesList);
        model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);
        return "Cooking/addImage";
    }


    @PostMapping("/SaveRecipe")
    public String saveRecipe (
            Model model,  @ModelAttribute Cooking cooking,
            @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
            @RequestParam(required=false) Collection<String> selectedIngredientReplacesList,
            @RequestParam("file") List<MultipartFile> file) throws IOException//несколько
        {
            String path= System.getProperty("user.dir")+"\\src\\main\\resources\\static\\Images";

            cookingService.postCooking(cooking);
            List<Cooking> cookingList = cookingService.list();
            Long cookingId =cookingList.stream().count();
            Cooking cookingForSaveOther=cookingList.get((int) (cookingId-1));

            //List<IngredientReplacement> ingredientReplacementList=new ArrayList<>();
            if(selectedIngredientReplacesList!=null){
                for (var item:selectedIngredientReplacesList) {
                    String[] data = item.split("/");
                    ingredientReplacementService.postIngredientReplacement(new IngredientReplacement(null,ingredientService.getIngredientById(Long.valueOf(data[0])).get(),ingredientService.getIngredientById(Long.valueOf(data[2])).get(),cookingForSaveOther, BigDecimal.valueOf(Float.valueOf(data[1])),  data[3]));
                }
            }

            if(selectedIngredientDishesList!=null){
                for (var item:selectedIngredientDishesList) {
                    String[] data=item.split("/");
                     ingredientDishService.postIngredientDish(new IngredientDish(null,ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,cookingForSaveOther,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                }
            }

            if (file != null){
                for(MultipartFile image:file){
                    if (file != null && !image.getOriginalFilename().isEmpty()) {
                        File uploadDir = new File(path);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        String uuidFile = UUID.randomUUID().toString();
                        String resultFilename = uuidFile + "." + image.getOriginalFilename();
                        image.transferTo(new File(path + "\\" + resultFilename));
                        //imageList.add(new CookingPhoto(null,null,path + "/" + resultFilename));pho
                        cookingPhotoService.postCookingPhoto(new CookingPhoto(null,cooking, resultFilename));

                    }
                }
            }

        return "redirect:/cooking";
    }


    @GetMapping("/View/{id}")
    public String cookingDitailView(Model model, @PathVariable("id") Long id) throws IOException {
        model.addAttribute("activePage", "Cooking");
        model.addAttribute("cooking", cookingService.getCookingById(id).get());

        String time =cookingService.getCookingById(id).get().getTimeCooking();
        Integer hour=Integer.parseInt(time.substring(0, time.indexOf(":")));
        time = time.substring(time.indexOf(":") + 1);
        Integer min= Integer.parseInt(time.substring(0, time.indexOf(":")));
        String timeRes = "";
        if(hour!=0){
            timeRes=hour+" ч ";
        }
        if(min!=0){
            timeRes=timeRes+min+" м ";
        }
        model.addAttribute("time",timeRes.trim());

        List<String> photoList= new ArrayList<>();
        //List<byte[]> photo1List= new ArrayList<>();
        List<CookingPhoto> cookingPhotoList=cookingPhotoService.getPhotoById(id);
        if(cookingPhotoList==null||cookingPhotoList.stream().count()==0){

            String path= System.getProperty("user.dir")+"\\src\\main\\resources\\static\\Images\\baseImage.jpg";
            File fi = new File(path);
            byte[] fileContent = Files.readAllBytes(fi.toPath());
            String CatImage =  Base64.encodeBase64String(fileContent);
            photoList.add(CatImage);

        }
        else{
            for (var item:cookingPhotoList){
                String path= System.getProperty("user.dir")+"\\src\\main\\resources\\static\\Images\\"+item.getFilename();

                File fi = new File(path);
                byte[] fileContent = Files.readAllBytes(fi.toPath());
                String CatImage =  Base64.encodeBase64String(fileContent);

                photoList.add(CatImage);
                //photoList.add(item.getFilename());
            }//4699342e-63db-4169-b110-7b4053bf2e11.bliny-na-moloke-tonkie-s-dyrochkami_1626675913_1_max.jpg

        }

        model.addAttribute("selectedPhoto", photoList.get(0));
        model.addAttribute("photos", IntStream.range(0, photoList.size())
                .filter(n -> n > 0)
                .mapToObj(photoList::get)
                .collect(Collectors.toList()));
        model.addAttribute("ingredientDishes", cookingService.getCookingById(id).get().getIngredientDishes());
        model.addAttribute("ingredientReplaces", cookingService.getCookingById(id).get().getIngredientReplacements());

        //Category category = categoryService.findbyID(id);



        return "Cooking/view";
    }




    @GetMapping("/ChangeCooking/{id}")
    public String choiceDishCookingChange(Model model, @PathVariable("id") Long id){
        cookingStatic=cookingService.getCookingById(id).get();
        ingredientReplacementList=new ArrayList<>();
        ingredientDishList=new ArrayList<>();
        ingredientDishList.addAll( ingredientDishService.getIngredientDishByCooking_Id(cookingStatic.getId()));
        ingredientReplacementList.addAll( ingredientReplacementService.getIngredientReplacementByCooking_Id(cookingStatic.getId()));
        model.addAttribute("cooking",cookingService.getCookingById(id).get());
        return "Cooking/changeCooking";
    }

    @PostMapping("/ChangeCooking")
    public String choiceDishCookingChange(Model model){
        model.addAttribute("cooking",cookingStatic);
        return "Cooking/changeCooking";
    }


    @GetMapping("/ChangeAddIngredientInRecipeComeback")
    public String createCookingAddIngredientChangeC(Model model){
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientDish",new IngredientDish());
        List<Ingredient> ingredients=ingredientService.list();
        if(ingredientDishList!=null){
            for (var item:ingredientDishList) {

                //if(!ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)){
                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                ingredients.removeIf(x->x.getNameIngredient().equals(item.getIngredient().getNameIngredient()));
                //}
            }
        }
        model.addAttribute("ingredients",ingredients);
        model.addAttribute("selectedIngredientDishes",ingredientDishList);
        return "Cooking/addIngredientChange";
    }



    static List<IngredientDish> ingredientDishList=new ArrayList<>();
    static Cooking cookingStatic;
    static List<IngredientReplacement> ingredientReplacementList=new ArrayList<>();
    @PostMapping("/ChangeAddIngredient")
    public String createCookingChange(Model model, @ModelAttribute Cooking cooking){

        cookingStatic=cooking;
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredientDish",new IngredientDish());

        //List<IngredientDish> ingredientDishList=ingredientDishService.getIngredientDishByCooking_Id(cooking.getId());
        List<Ingredient> ingredients=ingredientService.list();
            for (var item:ingredientDishList) {
                ingredients.removeIf(x->x.getNameIngredient().equals(item.getIngredient().getNameIngredient()));
            }


        model.addAttribute("ingredients",ingredients);
        model.addAttribute("selectedIngredientDishes",ingredientDishList);

        return "Cooking/addIngredientChange";
    }


    @GetMapping("/ChangeAddIngredientInRecipe")
    public String createCookingAddIngredientChange(Model model){
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientDish",new IngredientDish());

        //List<IngredientDish> ingredientDishList=new ArrayList<>();
        List<Ingredient> ingredients=ingredientService.list();
        if(ingredientDishList!=null){
            for (var item:ingredientDishList) {
                //String[] data=item.split("/");
                //if(!ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)){
                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                ingredients.removeIf(x->x.getNameIngredient().equals(item.getIngredient().getNameIngredient()));
                //}
            }
        }
        model.addAttribute("ingredients",ingredients);
        model.addAttribute("selectedIngredientDishes",ingredientDishList);
        return "Cooking/addIngredientChange";
    }










    @PostMapping("/ChangeAddIngredientInRecipe")

    public String createCookingAddIngredientChange(Model model, @ModelAttribute Cooking cooking ,
                                                   @ModelAttribute @Valid IngredientDish ingredientDish,
                                                   @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                   @RequestParam(required=false) String action){//userName
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredientDish",new IngredientDish());
        if(action.equals("Добавить ингредиент")){
           // List<IngredientDish> ingredientDishList=new ArrayList<>();
            List<Ingredient> ingredients=ingredientService.list();
            if(selectedIngredientDishesList!=null){
                for (var item:selectedIngredientDishesList) {
                    String[] data=item.split("/");
                    //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                }
                ingredientDishList.add(ingredientDish);
                ingredients.removeIf(x->x.getNameIngredient().equals(ingredientDish.getIngredient().getNameIngredient()));
            }
            else{
                ingredientDishList.add(ingredientDish);
                ingredients.removeIf(x->x.getNameIngredient().equals(ingredientDish.getIngredient().getNameIngredient()));
            }
            model.addAttribute("ingredients",ingredients);
            model.addAttribute("selectedIngredientDishes",ingredientDishList);
            return "Cooking/addIngredientChange";
        }

        return "Cooking/addIngredientChange";
    }

    @PostMapping("/ChangeDeleteIngredientInRecipe")
    public String deleteCookingAddIngredientChange(Model model, @ModelAttribute Cooking cooking ,
                                                @RequestParam String nameIngredient,
                                                @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                @RequestParam(required=false) String action){
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredientDish",new IngredientDish());

        //List<IngredientDish> ingredientDishList=new ArrayList<>();
        List<Ingredient> ingredients=ingredientService.list();
        if(selectedIngredientDishesList!=null){
            for (var item:selectedIngredientDishesList) {
                String[] data=item.split("/");
                if(!ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)){

                    //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
                    ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
                }
            }
            ingredientDishList.removeIf(x->x.getIngredient().getNameIngredient().equals(nameIngredient));
            ingredientReplacementList.removeIf(x->x.getIngredientRep().getNameIngredient().equals(nameIngredient));
        }
        model.addAttribute("ingredients",ingredients);
        model.addAttribute("selectedIngredientDishes",ingredientDishList);
        return "Cooking/addIngredientChange";

    }

    @PostMapping("/ChangeAddIngredientReplace")
    public String changeCookingAddIngredientAddReplace(Model model,
                                                       @ModelAttribute Cooking cooking,
                                                       @RequestParam(required=false) Collection<String> selectedIngredientDishesList){



        model.addAttribute("cooking",cookingStatic);


        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(ingredientReplacementList!=null){
//            for (var item:ingredientReplacementList) {
//
//                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
//                ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(item.getIngredientRep().getId())).get());
//                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
//            }

        }
        ingredientsForReplace.addAll(ingredientDishList.stream().map(x->x.getIngredient()).toList());
        model.addAttribute("selectedIngredientReplaces",ingredientReplacementList);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientsR",ingredientsForReplace);
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        return "Cooking/addIngredientRChange";
    }


    @GetMapping("/ChangeAddIngredientRInRecipe")
    public String changeCookingAddIngredientRAdd(Model model){
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredients",ingredientService.list());
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        //model.addAttribute("selectedIngredientDishes",selectedIngredientDishesI);

        //List<IngredientReplacement> ingredientReplacementListList=new ArrayList<>();
//        if(selectedIngredientRI!=null){
//            for (var item:selectedIngredientRI) {
//                String[] data=item.split("/");
//                ingredientReplacementListList.add(new IngredientReplacement(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),ingredientService.getIngredientById(Long.valueOf(data[2])).get() ,data[3]));
//            }
//           // ingredientReplacementList.a
//            //ingredientReplacementListList.add(ingredientReplacement);
//        }

        model.addAttribute("selectedIngredientReplaces",ingredientReplacementList);

        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(selectedIngredientDishesI!=null){
//            for (var item:selectedIngredientDishesI) {
//                String[] data=item.split("/");
//                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
//
//                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
//            }
           //ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());

        }
        ingredientsForReplace.addAll(ingredientDishList.stream().map(x->x.getIngredient()).toList());
        model.addAttribute("ingredientsR",ingredientsForReplace);
        return "Cooking/addIngredientRChange";
    }

    //-----------------------------------------
    @PostMapping("/ChangeAddIngredientRInRecipe")

    public String createCookingAddIngredientRChange(Model model, @ModelAttribute Cooking cooking,
                                                 @ModelAttribute @Valid IngredientReplacement ingredientReplacement,
                                                 @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                 @RequestParam(required=false) Collection<String> selectedIngredientReplacesList,
                                                 @RequestParam(required=false) String action){//userName

        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        //model.addAttribute("selectedIngredientDishes",selectedIngredientDishesList);

        //List<IngredientReplacement> ingredientReplacementListList=new ArrayList<>();

        if(selectedIngredientReplacesList!=null){
//            for (var item:selectedIngredientReplacesList) {
//                String[] data=item.split("/");
//                ingredientReplacementListList.add(new IngredientReplacement(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),ingredientService.getIngredientById(Long.valueOf(data[2])).get() ,data[3]));
//            }
            ingredientReplacementList.add(ingredientReplacement);
        }
        else{
            ingredientReplacementList.add(ingredientReplacement);

        }

        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(selectedIngredientDishesList!=null){
//            for (var item:selectedIngredientDishesList) {
//                String[] data=item.split("/");
//                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
//                ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());
//                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
//            }

        }
        ingredientsForReplace.addAll(ingredientDishList.stream().map(x->x.getIngredient()).toList());
        model.addAttribute("ingredientsR",ingredientsForReplace);
        model.addAttribute("selectedIngredientReplaces",ingredientReplacementList);


        model.addAttribute("ingredients",ingredientService.list());

        return "Cooking/addIngredientRChange";
    }


    @PostMapping("/ChangeDeleteIngredientRInRecipe")
    public String deleteCookingAddIngredientRChange(Model model, @ModelAttribute Cooking cooking,
                                                 @RequestParam String nameIngredient,
                                                 @RequestParam String nameIngredientR,
                                                 @RequestParam(required=false) Collection<String> selectedIngredientDishesList,
                                                 @RequestParam(required=false) Collection<String> selectedIngredientReplacesList,
                                                 @RequestParam(required=false) String action){





        //L//ist<IngredientReplacement> ingredientReplacementListList=new ArrayList<>();

        if(selectedIngredientReplacesList!=null){
//            for (var item:selectedIngredientReplacesList) {
//                String[] data = item.split("/");
//
////                String nameI=ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient();
////                String nameIR=ingredientService.getIngredientById(Long.valueOf(data[2])).get().getNameIngredient();
////                boolean I=nameI.equals(nameIngredient);
////                boolean Ir=nameIR.equals(nameIngredientR);
////                boolean test=I&&Ir;
//
////                if (!(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient().equals(nameIngredient)&&ingredientService.getIngredientById(Long.valueOf(data[2])).get().getNameIngredient().equals(nameIngredientR))) {
////                    ingredientReplacementListList.add(new IngredientReplacement(ingredientService.getIngredientById(Long.valueOf(data[0])).get(), BigDecimal.valueOf(Float.valueOf(data[1])), ingredientService.getIngredientById(Long.valueOf(data[2])).get(), data[3]));
////                }
//
//            }
            ingredientReplacementList.removeIf(x->(x.getIngredientRep().getNameIngredient().equals(nameIngredientR)&&(x.getIngredient().getNameIngredient().equals(nameIngredient))));
            //ingredientReplacementListList.add(ingredientReplacement);
        }

        List<Ingredient> ingredientsForReplace=new ArrayList<>();
        if(selectedIngredientDishesList!=null){
//            for (var item:selectedIngredientDishesList) {
//                String[] data=item.split("/");
//                //ingredientDishList.add(new IngredientDish(ingredientService.getIngredientById(Long.valueOf(data[0])).get() ,BigDecimal.valueOf(Float.valueOf(data[1])),data[2]));
//                ingredientsForReplace.add(ingredientService.getIngredientById(Long.valueOf(data[0])).get());
//                //ingredients.removeIf(x->x.getNameIngredient().equals(ingredientService.getIngredientById(Long.valueOf(data[0])).get().getNameIngredient()));
//            }

        }
        ingredientsForReplace.addAll(ingredientReplacementList.stream().map(x->x.getIngredientRep()).toList());ingredientsForReplace.addAll(ingredientReplacementList.stream().map(x->x.getIngredientRep()).toList());
        model.addAttribute("ingredientsR",ingredientsForReplace);
        model.addAttribute("selectedIngredientReplaces",ingredientReplacementList);
        //model.addAttribute("selectedIngredientDishes",ingredientDishList);
        model.addAttribute("cooking",cookingStatic);
        model.addAttribute("ingredientReplacement",new IngredientReplacement());
        model.addAttribute("ingredients",ingredientService.list());
        return "Cooking/addIngredientRChange";
    }

    @PostMapping("/SaveEdit")
    public String SaveEdit(Model model){


        cookingService.postCooking(cookingStatic);
        List<IngredientDish> ingredientDishesForDelete= ingredientDishService.getIngredientDishByCooking_Id(cookingStatic.getId());
        for (IngredientDish ingredientDish:ingredientDishesForDelete){
            ingredientDishService.deleteIngredientDish(ingredientDish.getId());
        }

        List<IngredientReplacement> ingredientReplacementForDelete= ingredientReplacementService.getIngredientReplacementByCooking_Id(cookingStatic.getId());
        for (IngredientReplacement ingredientReplacement:ingredientReplacementForDelete){
            ingredientReplacementService.deleteIngredientIngredientReplacement(ingredientReplacement.getId());
        }

        if(ingredientReplacementList!=null){
            for (var item:ingredientReplacementList) {
                ingredientReplacementService.postIngredientReplacement(new IngredientReplacement(null,item.getIngredient(),item.getIngredientRep(),cookingStatic, BigDecimal.valueOf(Float.valueOf(String.valueOf(item.getValueIngredient()))),  item.getUnitMeasure()));
            }

        }

        if(ingredientDishList!=null){
            for (var item:ingredientDishList) {

                ingredientDishService.postIngredientDish(new IngredientDish(null,item.getIngredient() ,cookingStatic, BigDecimal.valueOf(Float.valueOf(String.valueOf(item.getValueIngredient()))),  item.getUnitMeasure()));
            }
        }

        //List<IngredientDish> ingredientDishList=new ArrayList<>();



        return "redirect:/cooking";

    }



}
