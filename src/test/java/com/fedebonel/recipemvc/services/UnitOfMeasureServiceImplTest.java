package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.UnitOfMeasureDto;
import com.fedebonel.recipemvc.mappers.UnitOfMeasureToUnitOfMeasureDto;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {

    UnitOfMeasureToUnitOfMeasureDto converterToCommand;
    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converterToCommand = new UnitOfMeasureToUnitOfMeasureDto();
        service = new UnitOfMeasureServiceImpl(repository, converterToCommand);
    }

    @Test
    void listAllUOM() {
        Set<UnitOfMeasure> uoms = new HashSet<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        uoms.add(uom1);
        uoms.add(uom2);

        when(repository.findAll()).thenReturn(uoms);
        Set<UnitOfMeasureDto> uomsFound = service.listAllUOM();

        assertEquals(uoms.size(), uomsFound.size());
        verify(repository, times(1)).findAll();
    }
}