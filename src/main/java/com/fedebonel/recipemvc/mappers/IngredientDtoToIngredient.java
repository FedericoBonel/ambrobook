package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;
import com.fedebonel.recipemvc.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientDtoToIngredient implements Converter<IngredientDto, Ingredient> {

    private final UnitOfMeasureDtoToUnitOfMeasure unitOfMeasureDtoToUnitOfMeasure;

    public IngredientDtoToIngredient(UnitOfMeasureDtoToUnitOfMeasure unitOfMeasureDtoToUnitOfMeasure) {
        this.unitOfMeasureDtoToUnitOfMeasure = unitOfMeasureDtoToUnitOfMeasure;
    }


    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientDto source) {
        if (source == null) return null;

        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUom(unitOfMeasureDtoToUnitOfMeasure.convert(source.getUom()));
        return ingredient;
    }
}
