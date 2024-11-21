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
        Label recordLabel = new Label("New Label");
        recordLabel.textProperty().bind(noteListModel.recordNumbersProperty());
        Label numberOfRecordsLabel = new Label("Displayed Records:");
        hBox.getChildren().addAll(textField, numberOfRecordsLabel, numberOfRecords(), recordLabel);
        return vBox;
    }

    private Node numberOfRecords() {
        Label numberOfRecords = new Label(String.valueOf(noteListModel.getNotes().size()));
        noteListModel.getNotes().addListener((ListChangeListener<NoteDTO>) change -> {
            numberOfRecords.setText(String.valueOf(noteListModel.getNotes().size()));
        });
        return numberOfRecords;
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