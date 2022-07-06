package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.CategoryDto;
import com.fedebonel.recipemvc.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryDtoTest {
    public static final Long ID_VALUE = 1L;
    public static final String NAME = "name";
    CategoryToCategoryDto converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new CategoryToCategoryDto();
    }

    @Test
    public void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    public void convert() throws Exception {
        //given
        Category category = new Category();
        category.setId(ID_VALUE);
        category.setName(NAME);

        //when
        CategoryDto categoryDto = converter.convert(category);

        //then
        assertEquals(ID_VALUE, categoryDto.getId());
        assertEquals(NAME, categoryDto.getName());

    }
}