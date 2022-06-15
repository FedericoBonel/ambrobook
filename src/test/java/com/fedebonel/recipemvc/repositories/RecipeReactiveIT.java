package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveIT {
    static final String RECIPE = "Burgers";

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Before
    public void setUp() throws Exception {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void insertObject() {
        Recipe recipe = new Recipe();
        recipe.setDescription(RECIPE);

        recipeReactiveRepository.save(recipe).block();

        assertEquals(1, recipeReactiveRepository.count().block());
        assertEquals(RECIPE, recipeReactiveRepository.findAll().blockFirst().getDescription());
    }
}
