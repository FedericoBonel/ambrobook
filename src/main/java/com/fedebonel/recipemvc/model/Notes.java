package com.fedebonel.recipemvc.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Notes POJO
 */
@Getter
@Setter
public class Notes {

    @Id
    private String id;
    private String recipeNotes;

}
