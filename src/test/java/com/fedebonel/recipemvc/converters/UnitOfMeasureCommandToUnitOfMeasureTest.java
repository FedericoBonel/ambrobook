package com.fedebonel.recipemvc.converters;

import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {

    public static final String UNIT = "unit";
    public static final String STRING_VALUE = "1L";

    UnitOfMeasureCommandToUnitOfMeasure converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();

    }

    @Test
    public void testNullParamter() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(STRING_VALUE);
        command.setUnit(UNIT);

        //when
        UnitOfMeasure uom = converter.convert(command);

        //then
        assertNotNull(uom);
        assertEquals(STRING_VALUE, uom.getId());
        assertEquals(UNIT, uom.getUnit());

    }
}