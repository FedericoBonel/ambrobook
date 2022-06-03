package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.services.ImageService;
import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    /**
     * Handles GET requests to show the image form to upload new recipe images
     */
    @GetMapping("/recipe/{recipeId}/image")
    public String showUploadImageForm(@PathVariable Long recipeId, Model model){
        log.debug("Showing image form for recipe: " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return "recipe/imageform";
    }

    /**
     * Handles POST requests to upload new recipe images
     */
    @PostMapping("/recipe/{recipeId}/image")
    public String uploadImage(@PathVariable Long recipeId, @RequestParam("imagefile") MultipartFile image){
        log.debug("Uploading an image for recipe: " + recipeId);
        imageService.saveRecipeImage(recipeId, image);
        return "redirect:/recipe/" + recipeId + "/show";
    }
}
