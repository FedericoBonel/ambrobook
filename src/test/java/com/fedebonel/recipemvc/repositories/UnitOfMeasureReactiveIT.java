package com.fedebonel.recipemvc.repositories;

import com.fedebonel.recipemvc.datainitializer.DataInitializer;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import com.fedebonel.recipemvc.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveIT {

    static final String UNIT = "Cup";

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void insertObject() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setUnit(UNIT);

        unitOfMeasureReactiveRepository.save(uom).block();

        assertEquals(1, unitOfMeasureReactiveRepository.count().block());
        assertEquals(UNIT, unitOfMeasureReactiveRepository.findByUnit(UNIT).block().getUnit());
    }
}
