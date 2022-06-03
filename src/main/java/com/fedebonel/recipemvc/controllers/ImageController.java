package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.ImageService;
import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    /**
     * Handles GET requests to render the recipe image
     */
    @GetMapping("/recipe/{recipeId}/image/render")
    public void renderRecipeImage(@PathVariable Long recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);

        byte[] finalImage = {};

        if (recipeCommand.getImage() != null) {
            // Transform the Byte array to a byte array
            finalImage = new byte[recipeCommand.getImage().length];
            int currByte = 0;
            for (Byte imageByte : recipeCommand.getImage()) {
                finalImage[currByte++] = imageByte;
            }
        }

        // Return it through the output stream of the response
        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(finalImage);
        IOUtils.copy(is, response.getOutputStream());
    }
}
