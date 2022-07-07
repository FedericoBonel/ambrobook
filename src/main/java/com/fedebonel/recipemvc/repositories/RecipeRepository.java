package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.model.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Recipe repository
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    List<Recipe> findByDescriptionContainingIgnoreCaseOrCategories_nameContainingIgnoreCase(String description,
                                                                                            String name);
}
