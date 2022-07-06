package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.CategoryCommand;

import java.util.List;

public interface CategoryService {
    List<CategoryCommand> findAllCommands();

    CategoryCommand findCommandById(Long id);
}
