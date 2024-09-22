package com.L2.repository.interfaces;

import com.L2.dto.NoteDTO;

import java.util.List;

public interface NoteRepository {
    List<NoteDTO> getAllNotes();

    boolean noteExists(NoteDTO note);

    int insertNote(NoteDTO note);

    void updateNote(NoteDTO note);

    int deleteNote(NoteDTO noteDTO);
}
