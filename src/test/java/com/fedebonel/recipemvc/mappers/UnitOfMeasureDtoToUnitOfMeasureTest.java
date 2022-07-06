package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.UnitOfMeasureDto;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureDtoToUnitOfMeasureTest {

    public static final String UNIT = "unit";
    public static final Long LONG_VALUE = 1L;

    UnitOfMeasureDtoToUnitOfMeasure converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new UnitOfMeasureDtoToUnitOfMeasure();

    }

    @Test
    public void testNullParamter() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new UnitOfMeasureDto()));
    }

    @Test
    public void convert() throws Exception {
        //given
        UnitOfMeasureDto command = new UnitOfMeasureDto();
        command.setId(LONG_VALUE);
        command.setUnit(UNIT);

        //when
        UnitOfMeasure uom = converter.convert(command);

        //then
        assertNotNull(uom);
        assertEquals(LONG_VALUE, uom.getId());
        assertEquals(UNIT, uom.getUnit());

    }
}