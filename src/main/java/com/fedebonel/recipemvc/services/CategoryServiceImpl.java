package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.CategoryDto;
import com.fedebonel.recipemvc.mappers.CategoryToCategoryDto;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDto converter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryDto converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    @Override
    public List<CategoryDto> findAllCommands() {
        List<CategoryDto> categories = new ArrayList<>();
        categoryRepository.findAll()
                .forEach(category -> categories.add(converter.convert(category)));
        return categories;
    }

    @Override
    public CategoryDto findCommandById(Long id) {
        return categoryRepository.findById(id)
                .map(converter::convert)
                .orElseThrow(NotFoundException::new);
    }
}
