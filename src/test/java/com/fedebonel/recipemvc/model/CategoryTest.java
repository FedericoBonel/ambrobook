package com.fedebonel.recipemvc.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        String idValue = "4L";

        category.setId(idValue);

        assertEquals(idValue, category.getId());
    }

    @Test
    void getName() {
    }

    @Test
    void getRecipes() {
    }
}