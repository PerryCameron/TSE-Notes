package com.L2.mvci_notelist;

import com.L2.mvci_notelist.components.NotesTable;
import com.L2.widgetFx.TextFieldFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10, 0, 10, 0));
        TextField textField = TextFieldFx.of(200, "Search");
        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(25, 50, 100, 150, 200);
        comboBox.getSelectionModel().select(1);

        hBox.getChildren().addAll(textField,comboBox);
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