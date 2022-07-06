package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.CategoryDto;
import com.fedebonel.recipemvc.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoToCategoryTest {

    public static final Long ID_VALUE = 1L;
    public static final String NAME = "name";
    CategoryDtoToCategory conveter;

    @BeforeEach
    public void setUp() {
        conveter = new CategoryDtoToCategory();
    }

    @Test
    public void testNullObject() {
        assertNull(conveter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(conveter.convert(new CategoryDto()));
    }

    @Test
    public void convert() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(ID_VALUE);
        categoryDto.setName(NAME);

        //when
        Category category = conveter.convert(categoryDto);

        //then
        assertEquals(ID_VALUE, category.getId());
        assertEquals(NAME, category.getName());
    }
}