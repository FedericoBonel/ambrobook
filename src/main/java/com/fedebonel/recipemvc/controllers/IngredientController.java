package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.services.IngredientService;
import com.fedebonel.recipemvc.services.RecipeService;
import com.fedebonel.recipemvc.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class IngredientController {

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
    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable Long recipeId, Model model){
        log.debug("Getting list of Ingredients for recipe: " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return "recipe/ingredient/list";
    }

    /**
     * Handles GET requests for viewing an ingredients of a recipe
     */
    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model) {
        log.debug("Getting the ingredient: " + ingredientId);
        model.addAttribute("ingredient", ingredientService.findCommandById(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    /**
     * Handles GET requests for viewing the ingredient form to create ingredients
     */
    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String createRecipeIngredient(@PathVariable Long recipeId, Model model){
        log.debug("Getting creation form for ingredients for the recipe: " + recipeId);

        // Find the recipe, check it exists, create the commmand and asign it to the recipe so that it can be handled
        // by the saveOrUpdate correctly in the service
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
        // TODO show 404 page if recipe is not existent

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        // Init the unit of measure for the new ingredient (So that it can be "displayed" in the form)
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUOM());
        return "recipe/ingredient/ingredientform";
    }

    /**
     * Handles GET requests for viewing the ingredient form to update ingredients
     */
    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model){
        log.debug("Getting update form for ingredient with id: " + ingredientId);
        model.addAttribute("ingredient", ingredientService.findCommandById(recipeId, ingredientId));
        model.addAttribute("uomList", unitOfMeasureService.listAllUOM());
        return "recipe/ingredient/ingredientform";
    }

    /**
     * Handles GET requests for deleting ingredients from recipes
     */
    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId) {
        log.debug("Deleting from recipe " + recipeId + " - ingredient: " + ingredientId);
        ingredientService.deleteById(recipeId, ingredientId);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

    /**
     * Handles POST requests for updating ingredients
     */
    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        log.debug("Updating/Saving recipe with ingredient: " + ingredientCommand.getDescription());
        IngredientCommand savedCommand = ingredientService.saveCommand(ingredientCommand);
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

}
