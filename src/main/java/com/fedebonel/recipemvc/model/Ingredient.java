package com.fedebonel.recipemvc.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Ingredient POJO
 */
@Getter
@Setter
public class Ingredient {

    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;

    public Ingredient() {
    }

    public Ingredient(String description,
                      BigDecimal amount,
                      UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

    public Ingredient(String id,
                      String description,
                      BigDecimal amount,
                      UnitOfMeasure uom) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }
}
