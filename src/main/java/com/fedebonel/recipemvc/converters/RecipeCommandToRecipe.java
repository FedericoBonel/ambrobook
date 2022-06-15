package com.fedebonel.recipemvc.converters;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryCommandToCategory;
    private final NotesCommandToNotes notesCommandToNotes;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryCommandToCategory,
                                 NotesCommandToNotes notesCommandToNotes,
                                 IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.categoryCommandToCategory = categoryCommandToCategory;
        this.notesCommandToNotes = notesCommandToNotes;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {
        if (source == null) return null;

        final Recipe recipe = new Recipe();
        if (source.getId() == null || source.getId().isEmpty()) {
            recipe.setId(null);
        } else {
            recipe.setId(source.getId());
        }
        recipe.setSource(source.getSources());
        recipe.setNote(notesCommandToNotes.convert(source.getNotes()));
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDirections(source.getDirections());
        recipe.setDescription(source.getDescription());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setUrl(source.getUrl());
        recipe.setServings(source.getServings());
        recipe.setImage(source.getImage());

        if (source.getCategories() != null && source.getCategories().size() > 0) {
            source.getCategories()
                    .forEach(categoryCommand -> recipe.getCategories().add(categoryCommandToCategory.convert(categoryCommand)));
        }
        if (source.getIngredients() != null && source.getIngredients().size() > 0) {
            source.getIngredients()
                    .forEach(ingredientCommand -> recipe.getIngredients().add(ingredientCommandToIngredient.convert(ingredientCommand)));
        }
        return recipe;
    }
}
