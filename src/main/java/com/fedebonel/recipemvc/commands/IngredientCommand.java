package com.fedebonel.recipemvc.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;
    @NotBlank
    private String description;
    @NotNull
    @Min(1)
    @Max(10000)
    private BigDecimal amount;
    @NotNull
    private UnitOfMeasureCommand uom;
}
