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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

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
    public void addToBottomOfList(ExecutorService executorService) {
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
        executorService.submit(addToBottomTask);
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
    public void addToTopOfList(ExecutorService executorService) {
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
        executorService.submit(addToTopTask);
    }

    /**
     * Performs a search for notes based on the provided search parameters or retrieves paginated notes if no search parameters are specified.
     * The method clears the current note list and executes the operation asynchronously using the provided {@link ExecutorService} to avoid
     * blocking the JavaFX Application Thread. If search parameters are present, it activates search mode and queries notes with scoring;
     * otherwise, it deactivates search mode and fetches paginated notes using the current page size and offset.
     * <p>
     * The results are added to the {@code noteListModel}'s note list on the JavaFX Application Thread upon successful completion.
     * Errors during execution are logged, ensuring the application remains responsive. This method is typically used to update the note
     * list in response to user search input or to reset to a paginated view when the search is cleared.
     * </p>
     *
     * @param executorService the {@link ExecutorService} used to execute the search or fetch task asynchronously.
     *                       Must not be {@code null} and should be properly managed to ensure shutdown on application close.
     * @throws IllegalStateException if the {@code noteListModel} or {@code noteRepo} is not properly initialized
     * @throws NullPointerException if {@code executorService} is {@code null}
     * @see NoteListModel
     * @see NoteRepositoryImpl
     * @see ExecutorService
     */
    public void searchParameters(ExecutorService executorService) {
        noteListModel.getNotes().clear();
        if (noteListModel.getSearchParameters().isEmpty()) noteListModel.setActiveSearch(false);
        else noteListModel.setActiveSearch(true);
        Task<List<NoteFx>> searchTask = new Task<>() {
            @Override
            protected List<NoteFx> call() {
                if (noteListModel.isActiveSearch()) {
                    return noteRepo.searchNotesWithScoring(noteListModel.getSearchParameters());
                } else {
                    return noteRepo.getPaginatedNotes(noteListModel.getPageSize(), noteListModel.getOffset());
                }
            }
        };
        searchTask.setOnSucceeded(event -> {
            noteListModel.getNotes().addAll(searchTask.getValue());
        });
        searchTask.setOnFailed(event -> {
            logger.error("Failed to search because: {}", searchTask.getException().getMessage());
        });
        executorService.submit(searchTask);
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
