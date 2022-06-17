package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUOM();

    Mono<UnitOfMeasureCommand> findById(String id);
}
