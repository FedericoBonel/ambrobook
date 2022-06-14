package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.model.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Recipe repository
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
