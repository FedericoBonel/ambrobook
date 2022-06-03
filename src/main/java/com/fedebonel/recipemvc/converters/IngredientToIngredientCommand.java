package com.fedebonel.recipemvc.converters;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand uomToUomCommand;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand uomToUomCommand) {
        this.uomToUomCommand = uomToUomCommand;
    }


    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {
        if (source == null) return null;

        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(source.getId());
        ingredientCommand.setUom(uomToUomCommand.convert(source.getUom()));
        ingredientCommand.setAmount(source.getAmount());
        ingredientCommand.setDescription(source.getDescription());
        if (source.getRecipe() != null) ingredientCommand.setRecipeId(source.getRecipe().getId());
        return ingredientCommand;
    }
}
