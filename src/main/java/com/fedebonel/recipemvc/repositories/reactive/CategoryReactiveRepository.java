package com.fedebonel.recipemvc.repositories.reactive;

import com.fedebonel.recipemvc.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {

    Mono<Category> findByName(String name);

}
