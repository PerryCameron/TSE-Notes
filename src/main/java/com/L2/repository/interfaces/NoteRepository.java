package com.L2.repository.interfaces;

import com.L2.dto.NoteDTO;

import java.util.List;

public interface NoteRepository {
    List<NoteDTO> getAllNotes();

    List<NoteDTO> getPaginatedNotes(int pageSize, int offset);

    boolean noteExists(NoteDTO note);

    int insertNote(NoteDTO note);

    // For creating a blank new note
    NoteDTO insertBlankNote();

    void updateNote(NoteDTO note);

    int deleteNote(NoteDTO noteDTO);

    List<NoteDTO> searchNotesWithScoring(String inputKeywords);
}
