package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
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
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@Controller
public class IngredientController {

    public static final String INGREDIENT_FORM_PATH = "recipe/ingredient/ingredientform";
    public static final String INGREDIENT_SHOW_PATH = "recipe/ingredient/show";
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
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting list of Ingredients for recipe: " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return "recipe/ingredient/list";
    }

    /**
     * Handles GET requests for viewing an ingredients of a recipe
     */
    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        log.debug("Getting the ingredient: " + ingredientId);
        model.addAttribute("ingredient", ingredientService.findCommandById(recipeId, ingredientId));
        return INGREDIENT_SHOW_PATH;
    }

    /**
     * Handles GET requests for viewing the ingredient form to create ingredients
     */
    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public Mono<String> createRecipeIngredient(@PathVariable String recipeId, Model model) {
        log.debug("Getting creation form for ingredients for the recipe: " + recipeId);

        // Find the recipe, check it exists, create the command and assign it to the recipe so that it can be handled
        // by the form correctly and the saveupdate as well
        return recipeService.findCommandById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id " + recipeId + " not found")))
                .map(recipe -> {
                    IngredientCommand ingredientCommand = new IngredientCommand();
                    ingredientCommand.setRecipeId(recipeId);
                    ingredientCommand.setUom(new UnitOfMeasureCommand());

                    model.addAttribute("ingredient", ingredientCommand);
                    model.addAttribute("uomList", unitOfMeasureService.listAllUOM());

                    return recipe;
                }).thenReturn(INGREDIENT_FORM_PATH);
    }

    /**
     * Handles GET requests for viewing the ingredient form to update ingredients
     */
    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId,
                                         Model model) {
        log.debug("Getting update form for ingredient with id: " + ingredientId);
        model.addAttribute("ingredient", ingredientService.findCommandById(recipeId, ingredientId));
        model.addAttribute("uomList", unitOfMeasureService.listAllUOM());
        return INGREDIENT_FORM_PATH;
    }

    /**
     * Handles GET requests for deleting ingredients from recipes
     */
    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public Mono<String> deleteRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {
        log.debug("Deleting from recipe " + recipeId + " - ingredient: " + ingredientId);
        return ingredientService.deleteById(recipeId, ingredientId)
                .thenReturn("redirect:/recipe/" + recipeId + "/ingredients");
    }

    /**
     * Handles POST requests for updating ingredients
     */
    @PostMapping("recipe/{recipeId}/ingredient")
    public Mono<String> saveOrUpdate(@ModelAttribute("ingredient") @Valid Mono<IngredientCommand> ingredientCommand,
                                     @PathVariable String recipeId, Model model) {

        return ingredientCommand.map(ingredient -> ingredient)
                .doOnNext(ingredient -> log.debug("Updating/Saving recipe with ingredient: " + ingredient.getDescription()))
                .flatMap(ingredientService::saveCommand)
                .thenReturn("redirect:/recipe/" + recipeId + "/ingredients/")
                .onErrorResume(throwable -> {
                    model.addAttribute("uomList", unitOfMeasureService.listAllUOM());
                    ((IngredientCommand) model.getAttribute("ingredient")).setRecipeId(recipeId);
                    return Mono.just(INGREDIENT_FORM_PATH);
                }).doOnError(thr -> log.debug("Error while saving ingredient"));
    }

}
