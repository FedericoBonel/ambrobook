package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.services.RecipeService;
import com.fedebonel.recipemvc.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * Controller of the index page
 */
@Slf4j
@Controller
@RequestMapping("")
public class IndexController {
    public static final String INDEX_VIEW_PATH = "index";
    private final RecipeService recipeService;
    private final UserService userService;

    public IndexController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    /**
     * Handler for main page
     */
    @GetMapping({"", "index.html"})
    public String getIndexPage(Model model, @RequestParam(value = "q", required = false) String query) {
        log.debug("Getting index page");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto userDto = userService.findByUsername(username);

        if (userDto != null) {
            model.addAttribute("likedRecipes", userDto.getLikedRecipes());
        } else {
            model.addAttribute("likedRecipes", new ArrayList<>());
        }

        if (query == null) {
            model.addAttribute("recipes", recipeService.getRecipes());
        } else {
            model.addAttribute("recipes", recipeService.findByQuery(query));
        }

        return INDEX_VIEW_PATH;
    }
}
