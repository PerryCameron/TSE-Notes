package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

public class NoteListModel {
    // reference to mainModel notes
    private ObservableList<NoteDTO> notes;
    // this is the note that is selected in the TableView
    private ObjectProperty<NoteDTO> selectedNote = new SimpleObjectProperty<>();
    // reference to mainModel boundNote;
    protected ObjectProperty<NoteDTO> boundNote;
    // reference to noteModel --- allow this many records to be displayed
    private IntegerProperty pageSize = null;
    // reference to noteModel --- skip the first N records
    private IntegerProperty offset = null;
    private ObjectProperty<TableView<NoteDTO>> noteTable = new SimpleObjectProperty<>();
    private BooleanProperty refreshTable = new SimpleBooleanProperty(false);


    public int getPageSize() {
        return pageSize.get();
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
