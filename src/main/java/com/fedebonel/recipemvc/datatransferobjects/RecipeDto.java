package com.fedebonel.recipemvc.datatransferobjects;

import com.fedebonel.recipemvc.model.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RecipeDto {
    private Long id;

    @NotBlank
    @Size(min = 5, max = 200)
    private String description;

    @Min(0)
    @Max(3000)
    @NotNull
    private Integer prepTime;

    @Min(0)
    @Max(3000)
    @NotNull
    private Integer cookTime;

    @Min(1)
    @Max(50)
    @NotNull
    private Integer servings;

    private String sources;

    @URL
    private String url;

    @NotBlank
    private String directions;

    private Set<IngredientDto> ingredients = new HashSet<>();
    private Difficulty difficulty;
    private NotesDto notes;
    private List<CategoryDto> categories = new ArrayList<>();
    private Byte[] image;

    public List<Long> getCategoriesIds() {
        return categories
                .stream()
                .map(CategoryDto::getId)
                .collect(Collectors.toList());
    }
}
