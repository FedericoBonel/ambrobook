package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;
import com.fedebonel.recipemvc.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class IngredientToIngredientDto implements Converter<Ingredient, IngredientDto> {

    private final UnitOfMeasureToUnitOfMeasureDto uomToUomCommand;

    public IngredientToIngredientDto(UnitOfMeasureToUnitOfMeasureDto uomToUomCommand) {
        this.uomToUomCommand = uomToUomCommand;
    }


    @Synchronized
    @Nullable
    @Override
    public IngredientDto convert(Ingredient source) {
        if (source == null) return null;

        final IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(source.getId());
        ingredientDto.setUom(uomToUomCommand.convert(source.getUom()));
        ingredientDto.setAmount(source.getAmount());
        ingredientDto.setDescription(source.getDescription());
        if (source.getRecipe() != null) ingredientDto.setRecipeId(source.getRecipe().getId());
        return ingredientDto;
    }
}
