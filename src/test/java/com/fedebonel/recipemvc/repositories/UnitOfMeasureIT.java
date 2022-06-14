package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.datainitializer.DataInitializer;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by jt on 6/17/17.
 */

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {

        recipeRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        categoryRepository.deleteAll();

        // Emulate bean initialization
        DataInitializer recipeBootstrap = new DataInitializer(recipeRepository, unitOfMeasureRepository, categoryRepository);

        recipeBootstrap.onApplicationEvent(null);
    }

    @Test
    public void findByDescription() {

        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUnit("Teaspoon");

        assertEquals("Teaspoon", uomOptional.get().getUnit());
    }

    @Test
    public void findByDescriptionCup() {

        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUnit("Cup");

        assertEquals("Cup", uomOptional.get().getUnit());
    }

}
