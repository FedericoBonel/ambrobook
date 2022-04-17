package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test of the repository with the spring context and database
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByUnitTeaspoon() {
        String expected = "Teaspoon";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUnit("Teaspoon");
        assertEquals(expected, uomOptional.get().getUnit());
    }

    @Test
    void findByUnitCup() {
        String expected = "Cup";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUnit("Cup");
        assertEquals(expected, uomOptional.get().getUnit());
    }
}