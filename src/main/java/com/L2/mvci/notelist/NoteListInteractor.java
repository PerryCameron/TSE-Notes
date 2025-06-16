package com.L2.mvci.notelist;

import com.L2.dto.NoteFx;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.static_tools.ApplicationPaths;
import com.L2.widgetFx.DialogueFx;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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

    protected void selectNote() {
        selectTableRow(findNoteFromListMatchingBoundNote());
    }

    protected void displayPreviousNote() {
        int index = getIndexById(noteListModel.getBoundNote().getId());
        if (index < noteListModel.getNotes().size() - 1) {
            NoteFx noteDTO = noteListModel.getNotes().get(index + 1);
            selectTableRow(noteDTO);
        }
    }

    protected void displayNextNote() {
        int index = getIndexById(noteListModel.getBoundNote().getId());
        if (index > 0) {
            NoteFx noteDTO = noteListModel.getNotes().get(index - 1);
            selectTableRow(noteDTO);
        }
    }

    private int getIndexById(int id) {
        for (NoteFx note : noteListModel.getNotes()) {
            if (note.getId() == id) {
                return noteListModel.getNotes().indexOf(note);
            }
        }
        return -1;
    }

    protected void sortTableView() {
        logger.debug("Sorting table view");
        noteListModel.getNotes().sort(Comparator.comparing(NoteFx::getTimestamp).reversed());
    }

    protected void refreshTableView() {
        logger.debug("Refreshing table view");
        noteListModel.refreshTable();
    }

    protected void updateRange() {
        noteListModel.setRecordNumbers(noteListModel.getNotes().getLast().prettyDate() + " - " + noteListModel.getNotes().getFirst().prettyDate());
    }

    /**
     * Adds a new set of notes to the bottom of the paginated note list by fetching later notes
     * based on the current offset and page size. The method operates asynchronously using a
     * background task to avoid blocking the JavaFX Application Thread. If the oldest note is
     * reached, no new notes are added. The list is trimmed to maintain a maximum size, and the
     * scrollbar is adjusted to keep the view centered after adding notes.
     * <p>
     * This method updates the {@code noteListModel}'s offset and notes list, ensuring thread-safe
     * modifications on the JavaFX Application Thread. Errors during note retrieval are logged and
     * displayed to the user via an error dialog, with the offset restored to its previous value.
     * </p>
     *
     * @throws IllegalStateException if the note list model or note repository is not properly initialized
     * @see NoteListModel
     * @see NoteRepositoryImpl
     */
    public void addToBottomOfList() {
        if (noteListModel.getIsLoading().getAndSet(true)) {
            logger.debug("Skipping addToBottomOfList: another load is in progress");
            return;
        }
        int originalOffset = noteListModel.getOffset();
        int newOffset = originalOffset + noteListModel.getPageSize(); // Use pageSize for consistent pagination
        Task<List<NoteFx>> addToBottomTask = new Task<>() {
            @Override
            protected List<NoteFx> call() {
                try {
                    if (!noteRepo.isOldest(noteListModel.getNotes().getLast())) {
                        logger.debug("Fetching {} notes at offset {}", noteListModel.getPageSize(), newOffset);
                        List<NoteFx> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), newOffset);
                        return notes.isEmpty() ? Collections.emptyList() : notes;
                    } else {
                        logger.debug("Reached oldest note");
                        return Collections.emptyList();
                    }
                } catch (Exception e) {
                    logger.error("Error fetching bottom notes: {}", e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        };
        addToBottomTask.setOnSucceeded(event -> {
            try {
                List<NoteFx> notes = addToBottomTask.getValue();
                if (!notes.isEmpty()) {
                    noteListModel.offsetProperty().set(newOffset);
                    ObservableList<NoteFx> currentNotes = noteListModel.getNotes();
                    currentNotes.addAll(notes);
                    // Adjust scrollbar
                    ScrollBar verticalScrollBar = (ScrollBar) noteListModel.getNoteTable().lookup(".scroll-bar");
                    if (verticalScrollBar != null) {
                        verticalScrollBar.setValue(noteListModel.getSCROLLBAR_MIDDLE());
                    }
                    // Trim excess notes from top
                    if (currentNotes.size() > noteListModel.getMAX_NOTES()) {
                        currentNotes.remove(0, currentNotes.size() - noteListModel.getMAX_NOTES());
                        logger.debug("Removed {} excess notes from top", currentNotes.size() - noteListModel.getMAX_NOTES());
                    }
                    updateRange();
                } else {
                    logger.debug("No new notes to add at offset {}", newOffset);
                }
            } finally {
                noteListModel.getIsLoading().set(false);
            }
        });
        addToBottomTask.setOnFailed(event -> {
            try {
                noteListModel.offsetProperty().set(originalOffset);
                DialogueFx.errorAlert("Error fetching records", addToBottomTask.getException().getMessage());
            } finally {
                noteListModel.getIsLoading().set(false);
            }
        });
        noteListModel.getExecutor().submit(addToBottomTask);
    }

    /**
     * Adds a new set of notes to the top of the paginated note list by fetching earlier notes
     * based on the current offset and page size. The method operates asynchronously using a
     * background task to avoid blocking the JavaFX Application Thread. If the newest note is
     * reached, the list is reset to a default set of notes. The list is trimmed to maintain a
     * maximum size, and the scrollbar is adjusted to keep the view centered after adding notes.
     * <p>
     * This method updates the {@code noteListModel}'s offset and notes list, ensuring thread-safe
     * modifications on the JavaFX Application Thread. Errors during note retrieval are logged and
     * displayed to the user via an error dialog, with the offset restored to its previous value.
     * </p>
     *
     * @throws IllegalStateException if the note list model or note repository is not properly initialized
     * @see NoteListModel
     * @see NoteRepositoryImpl
     */
    public void addToTopOfList() {
        if (noteListModel.getIsLoading().getAndSet(true)) {
            logger.debug("Skipping addToTopOfList: another load is in progress");
            return;
        }
        int originalOffset = noteListModel.getOffset();
        int newOffset = Math.max(0, noteListModel.getOffset() - noteListModel.getPageSize());
        Task<List<NoteFx>> addToTopTask = new Task<>() {
            @Override
            protected List<NoteFx> call() {
                try {
                    if (!noteRepo.isNewest(noteListModel.getNotes().getFirst())) {
                        logger.debug("Fetching {} notes at offset {}", noteListModel.getPageSize(), newOffset);
                        List<NoteFx> notes = noteRepo.getPaginatedNotes(noteListModel.getPageSize(), newOffset);
                        return notes.isEmpty() ? Collections.emptyList() : notes;
                    } else {
                        logger.debug("Reached newest note");
                        Platform.runLater(() -> {
                            ObservableList<NoteFx> notes = noteListModel.getNotes();
                            if (notes.size() > noteListModel.getTRIM_THRESHOLD()) {
                                notes.clear();
                                notes.addAll(noteRepo.getPaginatedNotes(noteListModel.getDEFAULT_PAGE_SIZE(), 0));
                                logger.debug("Reset to {} notes", noteListModel.getDEFAULT_PAGE_SIZE());
                            }
                        });
                        return Collections.emptyList();
                    }
                } catch (Exception e) {
                    logger.error("Error fetching top notes: {}", e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        };
        addToTopTask.setOnSucceeded(event -> {
            try {
                List<NoteFx> notes = addToTopTask.getValue();
                if (!notes.isEmpty()) {
                    noteListModel.offsetProperty().set(newOffset);
                    ObservableList<NoteFx> currentNotes = noteListModel.getNotes();
                    currentNotes.addAll(0, notes);
                    // Adjust scrollbar
                    ScrollBar verticalScrollBar = (ScrollBar) noteListModel.getNoteTable().lookup(".scroll-bar");
                    if (verticalScrollBar != null) {
                        verticalScrollBar.setValue(noteListModel.getSCROLLBAR_MIDDLE());
                    }
                    // Trim excess notes
                    if (currentNotes.size() > noteListModel.getMAX_NOTES()) {
                        currentNotes.remove(noteListModel.getMAX_NOTES(), currentNotes.size());
                        logger.debug("Removed {} excess notes from bottom", currentNotes.size() - noteListModel.getMAX_NOTES());
                    }
                    updateRange();
                } else {
                    logger.debug("No new notes to add at offset {}", newOffset);
                }
            } finally {
                noteListModel.getIsLoading().set(false);
            }
        });
        addToTopTask.setOnFailed(event -> {
            try {
                noteListModel.offsetProperty().set(originalOffset);
                DialogueFx.errorAlert("Error fetching records", addToTopTask.getException().getMessage());
            } finally {
                noteListModel.getIsLoading().set(false);
            }
        });
        noteListModel.getExecutor().submit(addToTopTask);
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

    // good practice to shut down executor when closing app
    public void shutdown() {
        noteListModel.getExecutor().shutdown();
    }
}
