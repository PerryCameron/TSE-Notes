package com.L2.mvci.notelist;

import com.L2.dto.NoteFx;
import com.L2.mvci.notelist.components.NotesTable;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.TitleBarFx;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class NoteListView implements Builder<Region> {

    private final NoteListModel noteListModel;
    private final NotesTable notesTable;
    Consumer<NoteListMessage> action;
    private static final Logger logger = LoggerFactory.getLogger(NoteListView.class);

    public NoteListView(NoteListModel noteListModel, Consumer<NoteListMessage> m) {
        this.noteListModel = noteListModel;
        this.notesTable = new NotesTable(this);
        action = m;
    }

    @Override
    public Region build() {
        VBox root = new VBox(10);
        root.getStyleClass().add("base-vbox");
        root.setPadding(new Insets(10, 10, 0, 10));
        root.getChildren().addAll(navigation(), notesTable.build());
        noteListModel.refreshTableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                notesTable.refreshFields();
            }
        }) ;
        action.accept(NoteListMessage.UPDATE_RANGE_LABEL);
        return root;
    }

    private Node navigation() {
        VBox vBox = new VBox();
        HBox hBox = new HBox(10);
        vBox.getStyleClass().add("decorative-hbox");
        Button[] buttons = new Button[]{};
        vBox.getChildren().addAll(TitleBarFx.of("Find and Navigate", buttons), hBox);
        hBox.setPadding(new Insets(5, 0, 5, 5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(searchBox(), rangeLabel(), range(), recordBox());
        return vBox;
    }

    private Node searchBox() {
        Timeline debounce = new Timeline(new KeyFrame(Duration.seconds(1), event -> action.accept(NoteListMessage.SEARCH)));
        debounce.setCycleCount(1); // Ensures it only runs once after inactivity
        TextField textField = TextFieldFx.of(200, "Search");
        textField.textProperty().bindBidirectional(noteListModel.searchParametersProperty());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Search Made: {}", newValue);
            debounce.stop(); // Reset the timer
            debounce.playFromStart(); // Start the timer
        });

        return textField;
    }

    private Node recordBox() {
        HBox hBox = HBoxFx.iconBox(5);
        hBox.getChildren().addAll(numberOfRecordsLabel(), numberOfRecords());
        return hBox;
    }

    private Node numberOfRecordsLabel() {
        Label label = new Label("Notes:");
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
        label.getStyleClass().add("range-answer");
        return label;
    }

    private Node numberOfRecords() {
        Label label = new Label(String.valueOf(noteListModel.getNotes().size()));
        label.getStyleClass().add("prominent-answer");
        noteListModel.getNotes().addListener((ListChangeListener<NoteFx>) change -> label.setText(String.valueOf(noteListModel.getNotes().size())));
        return label;
    }

    public NoteListModel getNoteListModel() {
        return noteListModel;
    }

    public Consumer<NoteListMessage> getAction() {
        return action;
    }
}