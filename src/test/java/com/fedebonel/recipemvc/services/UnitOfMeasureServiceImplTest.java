package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import com.fedebonel.recipemvc.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {

    UnitOfMeasureToUnitOfMeasureCommand converterToCommand;
    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureReactiveRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converterToCommand = new UnitOfMeasureToUnitOfMeasureCommand();
        service = new UnitOfMeasureServiceImpl(repository, converterToCommand);
    }

    @Test
    void listAllUOM() {
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId("1L");
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId("2L");

        when(repository.findAll()).thenReturn(Flux.just(uom1, uom2));
        List<UnitOfMeasureCommand> uomsFound = service.listAllUOM().collectList().block();

        assertEquals(2, uomsFound.size());
        verify(repository, times(1)).findAll();
    }
}