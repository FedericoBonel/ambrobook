package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.NotesDto;
import com.fedebonel.recipemvc.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesDtoToNotesTest {
    public static final Long ID_VALUE = 1L;
    public static final String RECIPE_NOTES = "Notes";
    NotesDtoToNotes converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new NotesDtoToNotes();

    }

    @Test
    public void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new NotesDto()));
    }

    @Test
    public void convert() throws Exception {
        //given
        NotesDto notesDto = new NotesDto();
        notesDto.setId(ID_VALUE);
        notesDto.setRecipeNotes(RECIPE_NOTES);

        //when
        Notes notes = converter.convert(notesDto);

        //then
        assertNotNull(notes);
        assertEquals(ID_VALUE, notes.getId());
        assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
    }

}