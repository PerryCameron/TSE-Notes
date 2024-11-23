package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class NoteListModel {
    // reference to mainModel notes
    private ObservableList<NoteDTO> notes;
    // this is the note that is selected in the TableView
    private final ObjectProperty<NoteDTO> selectedNote = new SimpleObjectProperty<>();
    // reference to mainModel boundNote;
    protected ObjectProperty<NoteDTO> boundNote;
    // reference to noteModel --- allow this many records to be displayed
    private IntegerProperty pageSize = null;
    // reference to noteModel --- skip the first N records
    private IntegerProperty offset = null;
    private final ObjectProperty<TableView<NoteDTO>> noteTable = new SimpleObjectProperty<>();
    private final BooleanProperty refreshTable = new SimpleBooleanProperty(false);
    private final StringProperty recordNumbers = new SimpleStringProperty();
    private final StringProperty searchParameters = new SimpleStringProperty();
    private final BooleanProperty activeSearch = new SimpleBooleanProperty(false);


    public boolean isActiveSearch() {
        return activeSearch.get();
    }

    public BooleanProperty activeSearchProperty() {
        return activeSearch;
    }

    public void setActiveSearch(boolean activeSearch) {
        this.activeSearch.set(activeSearch);
    }

    public String getSearchParameters() {
        return searchParameters.get();
    }

    public StringProperty searchParametersProperty() {
        return searchParameters;
    }

    public void setSearchParameters(String searchParameters) {
        this.searchParameters.set(searchParameters);
    }

    public void setOffset(int offset) {
        this.offset.set(offset);
    }

    public boolean isRefreshTable() {
        return refreshTable.get();
    }

    public void setRefreshTable(boolean refreshTable) {
        this.refreshTable.set(refreshTable);
    }

    public String getRecordNumbers() {
        return recordNumbers.get();
    }

    public StringProperty recordNumbersProperty() {
        return recordNumbers;
    }

    public void setRecordNumbers(String recordNumbers) {
        this.recordNumbers.set(recordNumbers);
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    public IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public int getOffset() {
        return offset.get();
    }

    public void setOffsetProperty(IntegerProperty offset) {
        this.offset = offset;
    }

    public void setPageSizeProperty(IntegerProperty pageSize) {
        this.pageSize = pageSize;
    }

    public IntegerProperty offsetProperty() {
        return offset;
    }

    public NoteDTO getBoundNote() {
        return boundNote.get();
    }

    public ObjectProperty<NoteDTO> boundNoteProperty() {
        return boundNote;
    }

    public void setBoundNote(NoteDTO boundNote) {
        this.boundNote.set(boundNote);
    }

    public ObservableList<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<NoteDTO> notes) {
        this.notes = notes;
    }

    public NoteDTO getSelectedNote() {
        return selectedNote.get();
    }

    public ObjectProperty<NoteDTO> selectedNoteProperty() {
        return selectedNote;
    }

    public void setSelectedNote(NoteDTO selectedNote) {
        this.selectedNote.set(selectedNote);
    }

    public TableView<NoteDTO> getNoteTable() {
        return noteTable.get();
    }

    public ObjectProperty<TableView<NoteDTO>> noteTableProperty() {
        return noteTable;
    }

    public void setNoteTable(TableView<NoteDTO> noteTable) {
        this.noteTable.set(noteTable);
    }

    public BooleanProperty refreshTableProperty() {
        return refreshTable;
    }

    public void refreshTable() {
        this.refreshTableProperty().set(true);
        this.refreshTableProperty().set(false);
    }
}
