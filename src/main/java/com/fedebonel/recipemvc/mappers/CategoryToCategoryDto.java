package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.CategoryDto;
import com.fedebonel.recipemvc.model.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDto implements Converter<Category, CategoryDto> {

    @Synchronized
    @Nullable
    @Override
    public CategoryDto convert(Category source) {
        if (source == null) return null;

        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(source.getId());
        categoryDto.setName(source.getName());
        return categoryDto;
    }
}
