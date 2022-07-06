package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.NotesDto;
import com.fedebonel.recipemvc.model.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesDtoToNotes implements Converter<NotesDto, Notes> {

    @Synchronized
    @Nullable
    @Override
    public Notes convert(NotesDto source) {
        if (source == null) return null;

        final Notes notes = new Notes();
        notes.setId(source.getId());
        notes.setRecipeNotes(source.getRecipeNotes());
        return notes;
    }
}
