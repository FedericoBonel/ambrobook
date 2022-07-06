package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.CategoryCommand;
import com.fedebonel.recipemvc.converters.CategoryToCategoryCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryCommand converter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryCommand converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    @Override
    public List<CategoryCommand> findAllCommands() {
        List<CategoryCommand> categories = new ArrayList<>();
        categoryRepository.findAll()
                .forEach(category -> categories.add(converter.convert(category)));
        return categories;
    }

    @Override
    public CategoryCommand findCommandById(Long id) {
        return categoryRepository.findById(id)
                .map(converter::convert)
                .orElseThrow(NotFoundException::new);
    }
}
