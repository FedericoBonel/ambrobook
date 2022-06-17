package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/recipe")
public class RecipeController {

    public final static String RECIPE_SHOW_PATH = "recipe/show";
    public final static String RECIPE_FORM_PATH = "recipe/recipeform";

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Handles GET methods to view details of recipe
     */
    @GetMapping({"{id}/show"})
    public String showById(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return RECIPE_SHOW_PATH;
    }

    /**
     * Handles GET methods to access the recipe form to create a new recipe
     */
    @GetMapping({"/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return RECIPE_FORM_PATH;
    }

    /**
     * Handles GET methods to access the recipe form to update an existing recipe
     */
    @GetMapping({"{id}/update"})
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return RECIPE_FORM_PATH;
    }

    /**
     * Handles POST methods to update or save a recipe
     */
    @PostMapping
    public Mono<String> saveOrUpdate(@Valid @ModelAttribute("recipe") Mono<RecipeCommand> recipeCommand) {
        return recipeCommand.map(recipe -> recipe)
                .flatMap(recipeService::saveRecipeCommand)
                .map(savedRecipe -> "redirect:/recipe/" + savedRecipe.getId() + "/show")
                .onErrorResume(throwable -> Mono.just(RECIPE_FORM_PATH))
                .doOnError(throwable -> log.debug("Error while saving recipe"));
    }

    /**
     * Handles GET methods to delete recipes
     */
    @GetMapping({"{id}/delete"})
    public Mono<String> deleteById(@PathVariable String id) {
        log.debug("Deleted recipe: " + id);
        return recipeService.deleteById(id).thenReturn("redirect:/");
    }
}
