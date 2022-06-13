package com.fedebonel.recipemvc.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Unit of measure POJO
 */
@Data
@Entity
public class UnitOfMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String unit;

}
