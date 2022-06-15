package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import com.fedebonel.recipemvc.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand converter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand converter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.converter = converter;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUOM() {
        return unitOfMeasureRepository.findAll().map(converter::convert);
    }

    @Override
    public Mono<UnitOfMeasureCommand> findById(String id) {
        return unitOfMeasureRepository.findById(id).map(converter::convert);
    }
}
