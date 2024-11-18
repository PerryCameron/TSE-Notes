package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.static_tools.ApplicationPaths;
import javafx.beans.property.IntegerProperty;
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

    public void setBoundNoteProperty(ObjectProperty<NoteDTO> boundNote) {
        noteListModel.boundNote = boundNote;
    }

    public void setOffsetProperty(IntegerProperty offsetProperty) {
        noteListModel.setOffsetProperty(offsetProperty);
    }

    public void setPageSizeProperty(IntegerProperty pageSizeProperty) {
        noteListModel.setPageSizeProperty(pageSizeProperty);
    }

    public void updateBoundNote() {
        if(noteListModel.getSelectedNote() != null) {
            noteListModel.getBoundNote().copyFrom(noteListModel.getSelectedNote());
        }
    }

    private void selectTableRow(NoteDTO note) {
        noteListModel.getNoteTable().getSelectionModel().select(note);
    }

    private NoteDTO findNoteFromListMatchingBoundNote() {
        for(NoteDTO noteDTO: noteListModel.getNotes()) {
            if(noteListModel.getBoundNote().getId() == noteDTO.getId()) {
                return noteDTO;
            }
        }
        return null;
    }

    public void selectNote() {
        selectTableRow(findNoteFromListMatchingBoundNote());
    }

    public void displayPreviousNote() {
        int index = getIndexById(noteListModel.getBoundNote().getId());
        if (index < noteListModel.getNotes().size() - 1) {
            NoteDTO noteDTO = noteListModel.getNotes().get(index + 1);
            selectTableRow(noteDTO);
        }
        System.out.println();
    }

    public void displayNextNote() {
        int index = getIndexById(noteListModel.getBoundNote().getId());
        if (index > 0) {
            NoteDTO noteDTO = noteListModel.getNotes().get(index - 1);
            selectTableRow(noteDTO);
        }
        System.out.println();
    }

    public int getIndexById(int id) {
        for (NoteDTO note : noteListModel.getNotes()) {
            if (note.getId() == id) {
                return noteListModel.getNotes().indexOf(note);
            }
        }
        return -1;
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
