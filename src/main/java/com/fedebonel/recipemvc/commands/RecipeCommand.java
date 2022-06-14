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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private String id;

    @NotBlank
    @Size(min = 5, max = 200)
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

    private List<IngredientCommand> ingredients = new ArrayList<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private List<CategoryCommand> categories = new ArrayList<>();
    private Byte[] image;
}
