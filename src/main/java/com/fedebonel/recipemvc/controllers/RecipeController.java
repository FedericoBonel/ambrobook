package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.CategoryService;
import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
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

    public RecipeController(RecipeService recipeService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
    }

    /**
     * Handles GET methods to view details of recipe
     */
    @GetMapping({"/{id}/show"})
    public String showById(@PathVariable Long id, Model model) {
        Recipe foundRecipe = recipeService.findById(id);
        model.addAttribute("recipe", foundRecipe);
        return RECIPE_SHOW_PATH;
    }

    /**
     * Handles GET methods to access the recipe form to create a new recipe
     */
    @GetMapping({"/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        model.addAttribute("allCategories", categoryService.findAllCommands());
        model.addAttribute("selectedCategories", new ArrayList<>());
        return RECIPE_FORM_PATH;
    }

    /**
     * Handles GET methods to access the recipe form to update an existing recipe
     */
    @GetMapping({"/{id}/update"})
    public String updateRecipe(@PathVariable Long id, Model model) {
        RecipeCommand recipe = recipeService.findCommandById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("allCategories", categoryService.findAllCommands());
        model.addAttribute("selectedCategories", recipe.getCategoriesIds());
        return RECIPE_FORM_PATH;
    }

    /**
     * Handles POST methods to update or save a recipe
     */
    @PostMapping
    public String saveOrUpdate(@RequestParam(value = "checkedCategories", required = false) List<Long> checkedCategories,
                               @Valid @ModelAttribute("recipe") RecipeCommand recipeCommand,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAllCommands());
            model.addAttribute("selectedCategories", checkedCategories);
            return RECIPE_FORM_PATH;
        }

        if (checkedCategories != null) {
            for (Long categoryId : checkedCategories) {
                recipeCommand.getCategories().add(categoryService.findCommandById(categoryId));
            }
        }

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    /**
     * Handles GET methods to delete recipes
     */
    @GetMapping({"{id}/delete"})
    public String deleteById(@PathVariable Long id) {
        log.debug("Deleted recipe: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }
}
