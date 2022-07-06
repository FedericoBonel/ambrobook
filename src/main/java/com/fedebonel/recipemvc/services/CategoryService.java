package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAllCommands();

    CategoryDto findCommandById(Long id);
}
