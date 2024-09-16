package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;

public class NoteListModel {
    // reference to mainModel notes
    private ObservableList<NoteDTO> notes;
    private ObjectProperty<NoteDTO> selectedNote = new SimpleObjectProperty<>();
    // reference to mainModel boundNote;
    protected ObjectProperty<NoteDTO> boundNote;


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
}
