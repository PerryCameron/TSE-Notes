package com.L2.mvci_notelist;

import com.L2.mvci_notelist.components.NotesTable;
import com.L2.widgetFx.TextFieldFx;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;

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
        textField.textProperty().bindBidirectional(noteListModel.searchParametersProperty());
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        // this is awesome, stole from stackoverflow.com
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    pause.setOnFinished(event -> action.accept(NoteListMessage.SEARCH));
                    pause.playFromStart();
                }
        );
        Label recordLabel = new Label("New Label");
        recordLabel.textProperty().bind(noteListModel.recordNumbersProperty());
        hBox.getChildren().addAll(textField, recordLabel);
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