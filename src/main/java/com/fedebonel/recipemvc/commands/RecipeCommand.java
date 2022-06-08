package com.fedebonel.recipemvc.commands;

import com.fedebonel.recipemvc.model.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private Long id;

    @NotBlank
    @Size(min=5, max=200)
    private String description;

    @Min(0)
    @Max(3000)
    private Integer prepTime;

    @Min(0)
    @Max(3000)
    private Integer cookTime;

    @Min(1)
    @Max(10)
    private Integer servings;

    private String sources;

    @URL
    private String url;

    @NotBlank
    private String directions;

    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
    private Byte[] image;
}
