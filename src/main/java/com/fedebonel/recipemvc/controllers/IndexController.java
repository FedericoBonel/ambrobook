package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller of the index page
 */
@Slf4j
@Controller
@RequestMapping("")
public class IndexController {
    public static final String INDEX_VIEW_PATH = "index";
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Handler for main page
     */
    @GetMapping({"", "index.html"})
    public String getIndexPage(Model model, @RequestParam(value = "q", required = false) String query) {
        log.debug("Getting index page");
        if (query == null) {
            model.addAttribute("recipes", recipeService.getRecipes());
        } else {
            model.addAttribute("query", query);
            model.addAttribute("recipes", recipeService.findByQuery(query));
        }
        return INDEX_VIEW_PATH;
    }
}
