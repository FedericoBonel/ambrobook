package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.model.Category;
import com.fedebonel.recipemvc.repositories.reactive.CategoryReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveIT {
    static final String CATEGORY = "Japanese";

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Before
    public void setUp() throws Exception {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void insertObject() {
        Category cat = new Category();
        cat.setName(CATEGORY);

        categoryReactiveRepository.save(cat).block();

        assertEquals(1, categoryReactiveRepository.count().block());
        assertEquals(CATEGORY, categoryReactiveRepository.findByName(CATEGORY).block().getName());
    }
}
