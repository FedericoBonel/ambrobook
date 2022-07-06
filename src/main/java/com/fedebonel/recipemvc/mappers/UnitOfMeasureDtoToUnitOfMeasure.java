package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.UnitOfMeasureDto;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureDtoToUnitOfMeasure implements Converter<UnitOfMeasureDto, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureDto source) {
        if (source == null) return null;

        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(source.getId());
        unitOfMeasure.setUnit(source.getUnit());
        return unitOfMeasure;
    }
}
