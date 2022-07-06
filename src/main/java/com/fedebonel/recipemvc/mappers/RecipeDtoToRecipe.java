package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeDtoToRecipe implements Converter<RecipeDto, Recipe> {

    private final CategoryDtoToCategory categoryDtoToCategory;
    private final NotesDtoToNotes notesDtoToNotes;
    private final IngredientDtoToIngredient ingredientDtoToIngredient;

    public RecipeDtoToRecipe(CategoryDtoToCategory categoryDtoToCategory,
                             NotesDtoToNotes notesDtoToNotes,
                             IngredientDtoToIngredient ingredientDtoToIngredient) {
        this.categoryDtoToCategory = categoryDtoToCategory;
        this.notesDtoToNotes = notesDtoToNotes;
        this.ingredientDtoToIngredient = ingredientDtoToIngredient;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeDto source) {
        if (source == null) return null;

        final Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setSource(source.getSources());
        recipe.setNote(notesDtoToNotes.convert(source.getNotes()));
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
                    .forEach(categoryCommand -> recipe.getCategories().add(categoryDtoToCategory.convert(categoryCommand)));
        }
        if (source.getIngredients() != null && source.getIngredients().size() > 0) {
            source.getIngredients()
                    .forEach(ingredientCommand -> recipe.getIngredients().add(ingredientDtoToIngredient.convert(ingredientCommand)));
        }
        return recipe;
    }
}
