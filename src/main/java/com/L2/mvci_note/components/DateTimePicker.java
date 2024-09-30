package com.L2.mvci_note.components;

import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DateTimePicker implements Component<Region> {

    private final NoteView noteView;
    private final DatePicker datePicker;
    private final Spinner<Integer> hourSpinner;
    private final Spinner<Integer> minuteSpinner;
    private final VBox root;


    public DateTimePicker(NoteView noteView) {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
        this.datePicker = new DatePicker(LocalDate.now());
        this.hourSpinner = new Spinner<>();
        this.minuteSpinner = new Spinner<>();
        this.noteView = noteView;
        // Bind the datePicker, hourSpinner, and minuteSpinner to the timestamp in NoteDTO
    }


    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            datePicker.setValue(dateTime.toLocalDate());
            hourSpinner.getValueFactory().setValue(dateTime.getHour());
            minuteSpinner.getValueFactory().setValue(dateTime.getMinute());
        }
    }

    @Override
    public Region build() {
        root.getStyleClass().add("decorative-hbox");
        Button[] buttons = new Button[] { syncButton(), copyButton() };
        root.getChildren().addAll(TitleBarFx.of("Call Date/Time", buttons) ,dateTimePicker());
        refreshFields();
        return root;
    }

    private Button copyButton() {
        Button copyButton = ButtonFx.utilityButton( () -> {
            flash();
            noteView.getAction().accept(NoteMessage.COPY_NAME_DATE);
        }, "Copy", "/images/copy-16.png");
        copyButton.setTooltip(ToolTipFx.of("Copy User and Date/Time"));
        return copyButton;
    }

    private Button syncButton() {
        Button syncButton = ButtonFx.utilityButton( () -> {
            noteView.getNoteModel().setStatusLabel("Refreshing date and time to now.");
            setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
//            noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
        }, "Sync", "/images/sync-16.png");
        syncButton.setTooltip(ToolTipFx.of("Refresh date/time to now()"));
        return syncButton;
    }

    private Node dateTimePicker() {
        HBox hBox = new HBox(10);
        // Configure the spinners
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));

        hourSpinner.setPrefWidth(80);
        minuteSpinner.setPrefWidth(80);
        datePicker.setPrefWidth(150);

        // Label for separating hours and minutes
        Label colonLabel = new Label(":");
        colonLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16");

        // Add components to the HBox
        hBox.getChildren().addAll(datePicker, hourSpinner, colonLabel, minuteSpinner);
        hBox.setAlignment(Pos.CENTER);

        // Bind the dateTimeProperty to the current selection
        datePicker.setOnAction(event -> updateDateTime());
        hourSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDateTime());
        minuteSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDateTime());
        // Initialize with the current date and time
        return hBox;
    }

    private void updateDateTime() {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
        noteView.getNoteModel().getBoundNote().timestampProperty().set(LocalDateTime.of(date, time));
        noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
    }

    @Override
    public void flash() {
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle("")); // Reset the style
        pause.play();
    }

    @Override
    public void refreshFields() {
        LocalDateTime localDateTime = noteView.getNoteModel().getBoundNote().getTimestamp();
        datePicker.setValue(localDateTime.toLocalDate());
        hourSpinner.getValueFactory().setValue(localDateTime.getHour());
        minuteSpinner.getValueFactory().setValue(localDateTime.getMinute());
        setDateTime(noteView.getNoteModel().getBoundNote().getTimestamp());
    }
}











