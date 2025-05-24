package com.L2.repository.interfaces;

import com.L2.dto.NoteFx;

import java.util.List;

public interface NoteRepository {
    List<NoteFx> getAllNotes();

    List<NoteFx> getPaginatedNotes(int pageSize, int offset);

    boolean noteExists(NoteFx note);

    int insertNote(NoteFx note);

    // For creating a blank new note
    NoteFx insertBlankNote();

    void updateNote(NoteFx note);

    int deleteNote(NoteFx noteDTO);

    List<NoteFx> searchNotesWithScoring(String inputKeywords);
}
