package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.static_tools.ApplicationPaths;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteListInteractor implements ApplicationPaths {

    private static final Logger logger = LoggerFactory.getLogger(NoteListInteractor.class);
    private final NoteListModel noteListModel;
    private final NoteRepositoryImpl noteRepo;

    public NoteListInteractor(NoteListModel noteListModel) {
        this.noteListModel = noteListModel;
        this.noteRepo = new NoteRepositoryImpl();
    }

    public void setNotes(ObservableList<NoteDTO> notes) {
        noteListModel.setNotes(notes);
    }

    public void setBoundNote(ObjectProperty<NoteDTO> boundNote) {
        noteListModel.boundNote = boundNote;
    }

    public void updateBoundNote() {
//        noteListModel.getBoundNote().copyFrom(noteListModel.getSelectedNote());
    }


}
