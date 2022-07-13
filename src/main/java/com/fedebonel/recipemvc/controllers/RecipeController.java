package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.CategoryService;
import com.fedebonel.recipemvc.services.RecipeService;
import com.fedebonel.recipemvc.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(RecipeController.RECIPE_URI)
public class RecipeController {

    public static final String RECIPE_URI = "/recipe";
    public final static String RECIPE_SHOW_PATH = "recipe/show";
    public final static String RECIPE_FORM_PATH = "recipe/recipeform";

    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService,
                            CategoryService categoryService,
                            UserService userService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    /**
     * Handles GET methods to view details of recipe
     */
    @GetMapping({"/{id}/show"})
    public String showById(@PathVariable Long id, Model model) {
        Recipe foundRecipe = recipeService.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("recipe", foundRecipe);
        model.addAttribute("user", userService.findByUsername(username));
        return RECIPE_SHOW_PATH;
    }

    /**
     * Handles GET methods to access the recipe form to create a new recipe
     */
    @GetMapping({"/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeDto());
        model.addAttribute("allCategories", categoryService.findAllCommands());
        model.addAttribute("selectedCategories", new ArrayList<>());
        return RECIPE_FORM_PATH;
    }

    /**
     * Handles GET methods to access the recipe form to update an existing recipe
     */
    @GetMapping({"/{id}/update"})
    public String updateRecipe(@PathVariable Long id, Model model) {
        RecipeDto recipe = recipeService.findCommandById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("allCategories", categoryService.findAllCommands());
        model.addAttribute("selectedCategories", recipe.getCategoriesIds());
        return RECIPE_FORM_PATH;
    }

    /**
     * Handles POST methods to update or create a recipe
     */
    @PostMapping
    public String saveOrUpdate(@RequestParam(value = "checkedCategories", required = false) List<Long> checkedCategories,
                               @Valid @ModelAttribute("recipe") RecipeDto recipeDto,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAllCommands());
            if(checkedCategories != null ) {
                model.addAttribute("selectedCategories", checkedCategories);
            } else {
                model.addAttribute("selectedCategories", new ArrayList<>());
            }
            return RECIPE_FORM_PATH;
        }

        if (checkedCategories != null) {
            for (Long categoryId : checkedCategories) {
                recipeDto.getCategories().add(categoryService.findCommandById(categoryId));
            }
        }

        RecipeDto savedCommand = recipeService.saveRecipeCommand(recipeDto);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    /**
     * Handles GET methods to delete recipes
     */
    @GetMapping({"/{id}/delete"})
    public String deleteById(@PathVariable Long id) {
        log.debug("Deleted recipe: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

    /**
     * Handles POST methods to allow users to like recipes
     */
    @PostMapping("/{id}/user/like")
    public String likeById(@PathVariable Long id) {
        log.debug("User liking recipe with id: " + id);

        recipeService.saveRecipeLike(id);

        return "redirect:" + RECIPE_URI + "/" + id + "/show";
    }
}
