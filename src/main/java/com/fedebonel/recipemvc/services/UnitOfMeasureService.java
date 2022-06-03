package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUOM();
}
