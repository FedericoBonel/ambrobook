package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.UnitOfMeasureDto;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureDto> listAllUOM();
}
