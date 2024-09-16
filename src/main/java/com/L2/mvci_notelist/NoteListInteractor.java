package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import com.L2.mvci_main.MainModel;
import com.L2.static_tools.ApplicationPaths;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteListInteractor implements ApplicationPaths {

    private static final Logger logger = LoggerFactory.getLogger(NoteListInteractor.class);
    private final NoteListModel noteListModel;

    public NoteListInteractor(NoteListModel noteListModel) {
        this.noteListModel = noteListModel;

    }

    public void setReferences(ObservableList<NoteDTO> notes, NoteDTO boundNote) {
        noteListModel.setNotes(notes);
        noteListModel.setBoundNote(boundNote);
    }

    public void setNotes(ObservableList<NoteDTO> notes) {
        noteListModel.setNotes(notes);
    }
}
