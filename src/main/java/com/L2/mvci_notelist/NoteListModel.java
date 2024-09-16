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


    public ObservableList<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<NoteDTO> notes) {
        this.notes = notes;
    }
}
