package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeDto implements Converter<Recipe, RecipeDto> {

    private final CategoryToCategoryDto categoryToCategoryDto;
    private final NotesToNotesDto notesToNotesDto;
    private final IngredientToIngredientDto ingredientToIngredientDto;

    public RecipeToRecipeDto(CategoryToCategoryDto categoryToCategoryDto,
                             NotesToNotesDto notesToNotesDto,
                             IngredientToIngredientDto ingredientToIngredientDto) {
        this.categoryToCategoryDto = categoryToCategoryDto;
        this.notesToNotesDto = notesToNotesDto;
        this.ingredientToIngredientDto = ingredientToIngredientDto;
    }


    @Synchronized
    @Nullable
    @Override
    public RecipeDto convert(Recipe source) {
        if (source == null) return null;

        final RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(source.getId());
        recipeDto.setDescription(source.getDescription());
        recipeDto.setDifficulty(source.getDifficulty());
        recipeDto.setDirections(source.getDirections());
        recipeDto.setCookTime(source.getCookTime());
        recipeDto.setNotes(notesToNotesDto.convert(source.getNote()));
        recipeDto.setServings(source.getServings());
        recipeDto.setUrl(source.getUrl());
        recipeDto.setSources(source.getSource());
        recipeDto.setPrepTime(source.getPrepTime());
        recipeDto.setImage(source.getImage());

        if (source.getCategories() != null && source.getCategories().size() > 0) {
            source.getCategories()
                    .forEach(category -> recipeDto.getCategories().add(categoryToCategoryDto.convert(category)));
        }
        if (source.getIngredients() != null && source.getIngredients().size() > 0) {
            source.getIngredients()
                    .forEach(ingredient -> recipeDto.getIngredients().add(ingredientToIngredientDto.convert(ingredient)));
        }

        return recipeDto;
    }
}
