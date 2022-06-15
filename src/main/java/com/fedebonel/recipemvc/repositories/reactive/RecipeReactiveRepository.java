package com.fedebonel.recipemvc.repositories.reactive;

import com.fedebonel.recipemvc.model.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
