package com.fedebonel.recipemvc.converters;

import com.fedebonel.recipemvc.commands.CategoryCommand;
import com.fedebonel.recipemvc.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

    public static final String ID_VALUE = "1L";
    public static final String NAME = "name";
    CategoryCommandToCategory conveter;

    @BeforeEach
    public void setUp() {
        conveter = new CategoryCommandToCategory();
    }

    @Test
    public void testNullObject() {
        assertNull(conveter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(conveter.convert(new CategoryCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setName(NAME);

        //when
        Category category = conveter.convert(categoryCommand);

        //then
        assertEquals(ID_VALUE, category.getId());
        assertEquals(NAME, category.getName());
    }
}