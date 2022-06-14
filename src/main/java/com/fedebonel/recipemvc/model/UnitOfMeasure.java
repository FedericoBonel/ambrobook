package com.fedebonel.recipemvc.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Unit of measure POJO
 */
@Getter
@Setter
@Document
public class UnitOfMeasure {

    @Id
    private String id;
    private String unit;

}
