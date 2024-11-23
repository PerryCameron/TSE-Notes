package com.L2.mvci_notelist;

import com.L2.dto.NoteDTO;
import com.L2.mvci_notelist.components.NotesTable;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.TitleBarFx;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 10, 0, 10));
        root.getChildren().addAll(searchBox(), notesTable.build());
        noteListModel.refreshTableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getNotesTable().refreshFields();
            }
        }) ;
        action.accept(NoteListMessage.UPDATE_RANGE_LABEL);
        return root;
    }

    private Node searchBox() {
        VBox vBox = new VBox();
        HBox hBox = new HBox(10);
        vBox.getStyleClass().add("decorative-hbox");
        Button[] buttons = new Button[]{};
        vBox.getChildren().addAll(TitleBarFx.of("Find and Navigate", buttons), hBox);
        hBox.setPadding(new Insets(5, 0, 5, 5));
        hBox.setAlignment(Pos.CENTER_LEFT);
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

        hBox.getChildren().addAll(textField, rangeLabel(), range(), numberOfRecordsLabel(), numberOfRecords());
        return vBox;
    }

    private Node numberOfRecordsLabel() {
        Label label = new Label("Records:");
        label.setPadding(new Insets(0, 0, 0, 10));
        label.getStyleClass().add("prominent-label");
        return label;
    }

    private Node rangeLabel() {
        Label label = new Label("Range:");
        label.setPadding(new Insets(0, 0, 0, 10));
        label.getStyleClass().add("prominent-label");
        return label;
    }

    private Node range() {
        Label label = new Label("New Label");
        label.textProperty().bind(noteListModel.recordNumbersProperty());
        return label;
    }

    private Node numberOfRecords() {
        Label label = new Label(String.valueOf(noteListModel.getNotes().size()));
        label.getStyleClass().add("prominent-answer");
        noteListModel.getNotes().addListener((ListChangeListener<NoteDTO>) change -> {
            label.setText(String.valueOf(noteListModel.getNotes().size()));
        });
        return label;
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