package com.L2.mvci_notelist;

import com.L2.mvci_notelist.components.NotesTable;
import com.L2.widgetFx.TextFieldFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class NoteListView implements Builder<Region> {
    private final NoteListModel noteListModel;
    private final NotesTable notesTable;
    Consumer<NoteListMessage> action;

    public NoteListView(NoteListModel noteListModel, Consumer<NoteListMessage> m) {
        this.noteListModel = noteListModel;
        this.notesTable = new NotesTable(this);
        action = m;
    }

    @Override
    public Region build() {
        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 0, 10));
        root.getChildren().addAll(searchBox(), notesTable.build());
        noteListModel.refreshTableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getNotesTable().refreshFields();
            }
        }) ;
        return root;
    }

    private Node searchBox() {
        HBox hBox = new HBox();
        TextFieldFx.standardTextField(200, "Model");
        return hBox;
    }

    public NoteListModel getNoteListModel() {
        return noteListModel;
    }

    public Consumer<NoteListMessage> getAction() {
        return action;
    }

    public NotesTable getNotesTable() {
        return notesTable;
    }
}