package com.fedebonel.recipemvc.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Ingredient POJO
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure uom;
    @ManyToOne
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(String description,
                      BigDecimal amount,
                      UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

    public Ingredient(Long id,
                      String description,
                      BigDecimal amount,
                      UnitOfMeasure uom,
                      Recipe recipe) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.uom = uom;
        this.recipe = recipe;
    }
}
