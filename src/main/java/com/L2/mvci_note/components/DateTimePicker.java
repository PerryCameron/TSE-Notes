package com.L2.mvci_note.components;

import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteController;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private static final Logger logger = LoggerFactory.getLogger(DateTimePicker.class);


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
        Button[] buttons = new Button[]{syncButton(), copyButton()};
        root.getChildren().addAll(TitleBarFx.of("Call Date/Time", buttons), dateTimePicker());
        root.setOnMouseExited(event -> {
            updateDateTime();
        });
        return root;
    }

    private Button copyButton() {
        Button copyButton = ButtonFx.utilityButton(() -> {
            flash();
            noteView.getAction().accept(NoteMessage.COPY_NAME_DATE);
        }, "Copy", "/images/copy-16.png");
        copyButton.setTooltip(ToolTipFx.of("Copy User and Date/Time"));
        return copyButton;
    }

    private Button syncButton() {
        Button syncButton = ButtonFx.utilityButton(() -> {
            logger.info("Refreshing date and time to now.");
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);  // Include seconds when syncing
            setDateTime(now);
            // Explicitly update the note model with the seconds-precision time
            noteView.getNoteModel().getBoundNote().timestampProperty().set(now);
        }, "Sync", "/images/sync-16.png");
        syncButton.setTooltip(ToolTipFx.of("Refresh date/time to now()"));
        return syncButton;
    }


    private Node dateTimePicker() {
        HBox hBox = new HBox(10);

        // Configure the spinners
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));

        hourSpinner.setPrefWidth(80);
        minuteSpinner.setPrefWidth(80);
        datePicker.setPrefWidth(150);

        // Label for separating hours and minutes
        Label colonLabel = new Label(":");
        colonLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16");

        // Add components to the HBox
        hBox.getChildren().addAll(datePicker, hourSpinner, colonLabel, minuteSpinner);
        hBox.setAlignment(Pos.CENTER);
        refreshFields();
        // Bind the dateTimeProperty to the current selection

        return hBox;
    }

private void updateDateTime() {
    LocalDate date = datePicker.getValue();
    int hour = hourSpinner.getValue();
    int minute = minuteSpinner.getValue();
    // Get the current timestamp from the note
    LocalDateTime currentTimestamp = noteView.getNoteModel().getBoundNote().getTimestamp();
    // Preserve the seconds if they are present in the current timestamp; otherwise, default to 00
    int seconds = (currentTimestamp != null && currentTimestamp.getSecond() != 0) ? currentTimestamp.getSecond() : 0;
    // Create a new LocalTime using the provided hour, minute, and determined seconds value
    LocalTime time = LocalTime.of(hour, minute, seconds);
    // Update the timestamp with the new time values
    LocalDateTime dateTime = LocalDateTime.of(date, time);
    noteView.getNoteModel().getBoundNote().timestampProperty().set(dateTime);
    noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
    noteView.getAction().accept(NoteMessage.REFRESH_NOTE_TABLEVIEW);
    noteView.getAction().accept(NoteMessage.SORT_NOTE_TABLEVIEW);
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


//        datePicker.setOnAction(event -> {
//            System.out.println("DateTimePicker:dateTimePicker -> datePicker.setOnAction -> DateTimePicker::updateDateTime");
//            updateDateTime();
//        });
//
//
//        // these cause an extra time update when something programmatically changes them I don't like that
//        hourSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
//            System.out.println("DateTimePicker:dateTimePicker -> hourSpinner.setOnAction -> DateTimePicker::updateDateTime");
//            updateDateTime();
//        });
//
//        minuteSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
//            System.out.println("DateTimePicker:dateTimePicker -> hourSpinner.setOnAction -> DateTimePicker::updateDateTime");
//            updateDateTime();
//        });






