package com.L2.mvci.notelist;

import com.L2.dto.NoteFx;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.static_tools.ApplicationPaths;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollBar;
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

    public void setNotes(ObservableList<NoteFx> notes) {
        noteListModel.setNotes(notes);
    }

    public void setBoundNoteProperty(ObjectProperty<NoteFx> boundNote) {
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

    private void selectTableRow(NoteFx note) {
        noteListModel.getNoteTable().getSelectionModel().select(note);
    }

    private NoteFx findNoteFromListMatchingBoundNote() {
        for (NoteFx noteDTO : noteListModel.getNotes()) {
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
            NoteFx noteDTO = noteListModel.getNotes().get(index + 1);
            selectTableRow(noteDTO);
        }
    }

    public void displayNextNote() {
        int index = getIndexById(noteListModel.getBoundNote().getId());
        if (index > 0) {
            NoteFx noteDTO = noteListModel.getNotes().get(index - 1);
            selectTableRow(noteDTO);
        }
    }

    public int getIndexById(int id) {
        for (NoteFx note : noteListModel.getNotes()) {
            if (note.getId() == id) {
                return noteListModel.getNotes().indexOf(note);
            }
        }
        return -1;
    }

    public void sortTableView() {
        logger.debug("Sorting table view");
        noteListModel.getNotes().sort(Comparator.comparing(NoteFx::getTimestamp).reversed());
    }

    public void refreshTableView() {
        logger.debug("Refreshing table view");
        noteListModel.refreshTable();
    }

    public void addToBottomOfList() {
        int originalOffset = noteListModel.getOffset();
        int newOffset = noteListModel.getOffset() + noteListModel.getNotes().size();
        noteListModel.offsetProperty().set(newOffset);
        try {
            // If we haven't reached the olded note
            if (!noteRepo.isOldest(noteListModel.getNotes().getLast())) {
                logger.debug("We have not reached the oldest note");
                List<NoteFx> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), noteListModel.getOffset());
                logger.debug("We just queried {} notes that are offset by {}", noteListModel.getPageSize(), noteListModel.getOffset());
                if (!notes.isEmpty()) {
                    logger.debug("We are adding the " + notes.size() + " notes to the bottom of the list");
                    noteListModel.getNotes().addAll(notes);
                    // this helped a lot by moving the scroll bar to the middle
                    ScrollBar verticalScrollBar = (ScrollBar) noteListModel.getNoteTable().lookup(".scroll-bar");
                    if (verticalScrollBar != null) {
                        Platform.runLater(() -> verticalScrollBar.setValue(0.5));
                    }
                }
                else {
                    logger.debug("the notes list is empty, we are setting the offset to " + originalOffset);
                    noteListModel.offsetProperty().set(originalOffset); // Restore offset
                }
                if (noteListModel.getNotes().size() > 100) {
                    // Remove excess records from the top
                    noteListModel.getNotes().remove(0, noteListModel.getNotes().size() - 100); // Trim from top
                    logger.debug("We just removed 100 notes from the top");
                }
                updateRange();
            } else logger.debug("We have reached the oldest note");
        } catch (Exception e) {
            logger.debug("Error fetching bottom notes: {}", e.getMessage());
            noteListModel.offsetProperty().set(originalOffset); // Restore offset on failure
        }
    }

    public void addToTopOfList() {
        int newOffset = Math.max(0, noteListModel.getOffset() - noteListModel.getPageSize());
        noteListModel.offsetProperty().set(newOffset);
        try {
            if (!noteRepo.isNewest(noteListModel.getNotes().getFirst())) {
                logger.debug("We have not reached the newest note");
                List<NoteFx> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), newOffset);
                logger.debug("We just queried {} notes that are offset by {}", noteListModel.getPageSize(), noteListModel.getOffset());
                // Don't bother to add an empty list
                if (!notes.isEmpty()) {
                    logger.debug("We are adding the " + notes.size() + " notes to the top of the list");
                    noteListModel.getNotes().addAll(0, notes); // Add the new records at the top
                    // this helped a lot by moving the scroll bar to the middle
                    ScrollBar verticalScrollBar = (ScrollBar) noteListModel.getNoteTable().lookup(".scroll-bar");
                    if (verticalScrollBar != null) {
                        Platform.runLater(() -> verticalScrollBar.setValue(0.5));
                    }
                }
                if (noteListModel.getNotes().size() > 100)
                    // Remove excess records from the bottom
                    logger.debug("We just removed 100 notes from the bottom");
                    noteListModel.getNotes().remove(100, noteListModel.getNotes().size());
                updateRange();
            }  else {  logger.debug("We have reached the newest note");
                // we have reached the top lets take it back to 50
                if(noteListModel.getNotes().size() > 60) {
                    noteListModel.getNotes().clear();
                    noteListModel.getNotes().addAll(noteRepo.getPaginatedNotes(50, 0));
                    logger.debug("Stacking 50");
                }
            }
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
            noteListModel.setActiveSearch(false);
            List<NoteFx> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), noteListModel.getOffset());
            noteListModel.getNotes().addAll(notes);
        } else {
            noteListModel.setActiveSearch(true);
            List<NoteFx> notes = noteRepo.searchNotesWithScoring(noteListModel.getSearchParameters());
            noteListModel.getNotes().addAll(notes);
        }
    }

    // shouldn't need this but unfortunately we do
    public void updateTable() {
        noteListModel.getNoteTable().refresh();
    }

    public void logNoActionForScroll() {
        logger.warn("No action taken for scroll");
    }

    public void logNoActionForSearch() {
        logger.warn("No action taken for search");
    }

    public void logNoActionForKeyPress() {
        logger.warn("No action taken for this key press");
    }
}
