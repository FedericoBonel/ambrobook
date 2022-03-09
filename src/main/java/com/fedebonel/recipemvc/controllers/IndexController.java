package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller of the index page
 */
@Controller
@RequestMapping("")
public class IndexController {
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * When a http get request gets done to the server under the localhost:8080 endpoint
     * return the index page
     */
    @RequestMapping({"","index", "index.html"})
    public String getIndexPage(Model model){
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
