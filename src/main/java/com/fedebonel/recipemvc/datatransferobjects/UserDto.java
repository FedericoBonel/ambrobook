package com.fedebonel.recipemvc.datatransferobjects;

import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.model.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDto {
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 10)
    private String username;

    @NotBlank
    @Size(min = 4, max = 256)
    private String password;

    private Boolean active;

    private List<UserRole> userRoles;

    private List<Recipe> likedRecipes;
}
