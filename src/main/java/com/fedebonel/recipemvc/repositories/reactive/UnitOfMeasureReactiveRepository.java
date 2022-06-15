package com.fedebonel.recipemvc.repositories.reactive;

import com.fedebonel.recipemvc.model.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.Optional;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
}
