package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    /**
     * Handles GET methods to view details of recipe
     */
    @GetMapping({"{id}/show"})
    public String showById(@PathVariable Long id, Model model) {
        Recipe foundRecipe = recipeService.findById(id);
        model.addAttribute("recipe", foundRecipe);
        return "recipe/show";
    }

    /**
     * Handles GET methods to access the recipe form to create a new recipe
     */
    @GetMapping({"/new"})
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    /**
     * Handles GET methods to access the recipe form to update an existing recipe
     */
    @GetMapping({"{id}/update"})
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/recipeform";
    }

    /**
     * Handles POST methods to update or save a recipe
     */
    @PostMapping
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
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

    /**
     * Handles Not Found Exceptions
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFound(Exception exception) {
        log.debug("Handling not found exception in Recipe Controller");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", exception);
        modelAndView.setViewName("error/error404");
        return modelAndView;
    }
}
