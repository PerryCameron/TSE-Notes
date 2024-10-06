package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.static_tools.ApplicationPaths;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

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
        System.out.println("NoteListInteractor::updateBoundNote -> bound note copies selected note");
        if(noteListModel.getSelectedNote() != null) {
            noteListModel.getBoundNote().copyFrom(noteListModel.getSelectedNote());
        }
    }

    // when bound note changes information this is a message that gets sent to select a row in the table
    // the problem is that the bound note is not the actual object, so I use it to find the correct object
    public void selectBoundNoteAndUseToSelectRowInTable() {
        for(NoteDTO note: noteListModel.getNotes()) {
            if(noteListModel.getBoundNote().getId() == note.getId()) {
                noteListModel.getNoteTable().getSelectionModel().select(note);
            }
        }
    }

    public void sortTableView() {
        logger.info("Sorting table view");
        noteListModel.getNotes().sort(Comparator.comparing(NoteDTO::getTimestamp).reversed());
    }

    public void refreshTableView() {
        logger.info("Refreshing table view");
        noteListModel.refreshTable();
    }
}
