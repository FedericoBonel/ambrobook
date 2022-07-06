package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.services.IngredientService;
import com.fedebonel.recipemvc.services.RecipeService;
import com.fedebonel.recipemvc.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(IngredientController.RECIPE_INGREDIENTS_URI)
public class IngredientController {

    public static final String RECIPE_INGREDIENTS_URI = "/recipe/{recipeId}/ingredient";
    public static final String RECIPE_INGREDIENT_LIST_PATH = "recipe/ingredient/list";
    public static final String RECIPE_INGREDIENT_INGREDIENTFORM_PATH = "recipe/ingredient/ingredientform";
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    /**
     * Handles GET requests for viewing list of ingredients of a recipe
     */
    @GetMapping
    public String listIngredients(@PathVariable Long recipeId, Model model) {
        log.debug("Getting list of Ingredients for recipe: " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return RECIPE_INGREDIENT_LIST_PATH;
    }

    /**
     * Handles GET requests for viewing an ingredients of a recipe
     */
    @GetMapping("/{ingredientId}/show")
    public String showIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model) {
        log.debug("Getting the ingredient: " + ingredientId);
        model.addAttribute("ingredient", ingredientService.findCommandById(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    /**
     * Handles GET requests for viewing the ingredient form to create ingredients
     */
    @GetMapping("/new")
    public String createRecipeIngredient(@PathVariable Long recipeId, Model model) {
        log.debug("Getting creation form for ingredients for the recipe: " + recipeId);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUOM());
        return RECIPE_INGREDIENT_INGREDIENTFORM_PATH;
    }

    /**
     * Handles GET requests for viewing the ingredient form to update ingredients
     */
    @GetMapping("/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model) {
        log.debug("Getting update form for ingredient with id: " + ingredientId);
        model.addAttribute("ingredient", ingredientService.findCommandById(recipeId, ingredientId));
        model.addAttribute("uomList", unitOfMeasureService.listAllUOM());
        return RECIPE_INGREDIENT_INGREDIENTFORM_PATH;
    }

    /**
     * Handles GET requests for deleting ingredients from recipes
     */
    @GetMapping("/{ingredientId}/delete")
    public String deleteRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId) {
        log.debug("Deleting from recipe " + recipeId + " - ingredient: " + ingredientId);
        ingredientService.deleteById(recipeId, ingredientId);
        return "redirect:/recipe/" + recipeId + "/ingredient";
    }

    /**
     * Handles POST requests for updating ingredients
     */
    @PostMapping
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        log.debug("Updating/Saving recipe with ingredient: " + ingredientCommand.getDescription());
        IngredientCommand savedCommand = ingredientService.saveCommand(ingredientCommand);
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient";
    }

}
