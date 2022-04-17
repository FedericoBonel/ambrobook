package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @RequestMapping({"/show/{id}"})
    public String showById(@PathVariable String id, Model model){
        Recipe foundRecipe = recipeService.findById(Long.valueOf(id));
        model.addAttribute("recipe", foundRecipe);

        return "recipe/show";
    }
}
