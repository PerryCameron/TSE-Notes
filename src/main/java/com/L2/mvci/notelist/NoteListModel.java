package com.L2.mvci.notelist;

import com.L2.dto.NoteFx;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class NoteListModel {
    // reference to mainModel notes
    private ObservableList<NoteFx> notes;
    // this is the note that is selected in the TableView
    private final ObjectProperty<NoteFx> selectedNote = new SimpleObjectProperty<>();
    // reference to mainModel boundNote;
    protected ObjectProperty<NoteFx> boundNote;
    // reference to noteModel --- allow this many records to be displayed
    private IntegerProperty pageSize = null;
    // reference to noteModel --- skip the first N records
    private IntegerProperty offset = null;
    private final ObjectProperty<TableView<NoteFx>> noteTable = new SimpleObjectProperty<>();
    private final BooleanProperty refreshTable = new SimpleBooleanProperty(false);
    private final StringProperty recordNumbers = new SimpleStringProperty();
    private final StringProperty searchParameters = new SimpleStringProperty();
    private final BooleanProperty activeSearch = new SimpleBooleanProperty(false);

    // constants for paginate feature
    private final int MAX_NOTES = 100;
    private final int DEFAULT_PAGE_SIZE = 50;
    private final int TRIM_THRESHOLD = 60;
    private final double SCROLLBAR_MIDDLE = 0.5;
    private final AtomicBoolean isLoading = new AtomicBoolean(false);





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
    public NoteFx getBoundNote() {
        return boundNote.get();
    }
    public ObjectProperty<NoteFx> boundNoteProperty() {
        return boundNote;
    }
    public void setBoundNote(NoteFx boundNote) {
        this.boundNote.set(boundNote);
    }
    public ObservableList<NoteFx> getNotes() {
        return notes;
    }
    public void setNotes(ObservableList<NoteFx> notes) {
        this.notes = notes;
    }
    public NoteFx getSelectedNote() {
        return selectedNote.get();
    }
    public ObjectProperty<NoteFx> selectedNoteProperty() {
        return selectedNote;
    }
    public void setSelectedNote(NoteFx selectedNote) {
        this.selectedNote.set(selectedNote);
    }
    public TableView<NoteFx> getNoteTable() {
        return noteTable.get();
    }
    public ObjectProperty<TableView<NoteFx>> noteTableProperty() {
        return noteTable;
    }
    public void setNoteTable(TableView<NoteFx> noteTable) {
        this.noteTable.set(noteTable);
    }
    public BooleanProperty refreshTableProperty() {
        return refreshTable;
    }
    public void refreshTable() {
        this.refreshTableProperty().set(true);
        this.refreshTableProperty().set(false);
    }

    public int getMAX_NOTES() {
        return MAX_NOTES;
    }

    public int getDEFAULT_PAGE_SIZE() {
        return DEFAULT_PAGE_SIZE;
    }

    public int getTRIM_THRESHOLD() {
        return TRIM_THRESHOLD;
    }

    public double getSCROLLBAR_MIDDLE() {
        return SCROLLBAR_MIDDLE;
    }

    public AtomicBoolean getIsLoading() {
        return isLoading;
    }


}
