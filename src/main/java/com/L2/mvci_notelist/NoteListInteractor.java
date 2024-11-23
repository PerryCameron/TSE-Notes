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
import java.util.List;

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
        if (noteListModel.getSelectedNote() != null) {
            noteListModel.getBoundNote().copyFrom(noteListModel.getSelectedNote());
        }
    }

    private void selectTableRow(NoteDTO note) {
        noteListModel.getNoteTable().getSelectionModel().select(note);
    }

    private NoteDTO findNoteFromListMatchingBoundNote() {
        for (NoteDTO noteDTO : noteListModel.getNotes()) {
            if (noteListModel.getBoundNote().getId() == noteDTO.getId()) {
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
    }

    public void displayNextNote() {
        int index = getIndexById(noteListModel.getBoundNote().getId());
        if (index > 0) {
            NoteDTO noteDTO = noteListModel.getNotes().get(index - 1);
            selectTableRow(noteDTO);
        }
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

    public void addToBottomOfList() {
        int originalOffset = noteListModel.getOffset();
        int newOffset = noteListModel.getOffset() + noteListModel.getNotes().size();
        noteListModel.offsetProperty().set(newOffset);
        try {
            List<NoteDTO> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), noteListModel.getOffset());
            if (!notes.isEmpty()) noteListModel.getNotes().addAll(notes);
            else noteListModel.offsetProperty().set(originalOffset); // Restore offset
            if (noteListModel.getNotes().size() > 100) {
                noteListModel.getNotes().remove(0, noteListModel.getNotes().size() - 100); // Trim from top
            }
            updateRange();
        } catch (Exception e) {
            logger.error("Error fetching bottom notes: {}", e.getMessage());
            noteListModel.offsetProperty().set(originalOffset); // Restore offset on failure
        }
    }

    public void addToTopOfList() {
        int newOffset = Math.max(0, noteListModel.getOffset() - noteListModel.getPageSize());
        noteListModel.offsetProperty().set(newOffset);
        try {
        List<NoteDTO> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), newOffset);
        // Don't bother to add an empty list
        if (!notes.isEmpty())
            noteListModel.getNotes().addAll(0, notes); // Add the new records at the top
        if (noteListModel.getNotes().size() > 100)
            noteListModel.getNotes().remove(100, noteListModel.getNotes().size()); // Remove excess records from the bottom
        updateRange();
        } catch (Exception e) {
            logger.error("Error fetching top notes: {}", e.getMessage());
        }
    }

    public void updateRange() {
        noteListModel.setRecordNumbers(noteListModel.getNotes().getLast().prettyDate() + " - " + noteListModel.getNotes().getFirst().prettyDate());
    }

    public void searchParameters() {
        noteListModel.getNotes().clear();
        if (noteListModel.getSearchParameters().isEmpty()) {
            System.out.println("search bar is empty");
            List<NoteDTO> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), noteListModel.getOffset());
            noteListModel.getNotes().addAll(notes);
        } else {
            System.out.println("searching " + noteListModel.getSearchParameters());
            List<NoteDTO> notes = noteRepo.searchNotesWithScoring(noteListModel.getSearchParameters());
            noteListModel.getNotes().addAll(notes);
        }
    }
}
