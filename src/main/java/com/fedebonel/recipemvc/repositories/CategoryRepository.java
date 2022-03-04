package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Category Repository
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
