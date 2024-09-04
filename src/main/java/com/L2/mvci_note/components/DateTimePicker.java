package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateTimePicker implements Builder<Region> {

    private final NoteView noteView;
    private final DatePicker datePicker;
    private final Spinner<Integer> hourSpinner;
    private final Spinner<Integer> minuteSpinner;
    private VBox dateBox;


    public DateTimePicker(NoteView noteView) {
        this.datePicker = new DatePicker(LocalDate.now());
        this.hourSpinner = new Spinner<>();
        this.minuteSpinner = new Spinner<>();
        this.noteView = noteView;
    }

    private void updateDateTime() {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
        noteView.getNoteModel().dateTimePropertyProperty().set(LocalDateTime.of(date, time));
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
        this.dateBox = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
        dateBox.getStyleClass().add("decorative-hbox");
        Button[] buttons = new Button[] { refreshButton(), copyButton() };
        dateBox.getChildren().addAll(TitleBarFx.of("Call Date/Time", buttons) ,dateTimePicker());
        return dateBox;
    }

    private Button copyButton() {
        Button copyButton = ButtonFx.utilityButton("/images/copy-16.png", () -> {
            dateBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            // Use a PauseTransition to remove the border after 0.2 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> dateBox.setStyle("")); // Reset the style
            pause.play();
            noteView.getAction().accept(NoteMessage.COPY_NAME_DATE);
        });
        copyButton.setTooltip(ToolTipFx.of("Copy User and Date/Time"));
        return copyButton;
    }

    private Button refreshButton() {
        Button refreshButton = ButtonFx.utilityButton("/images/refresh-16.png", () -> {
            noteView.getNoteModel().setStatusLabel("Refreshing date and time to now.");
            setDateTime(LocalDateTime.now());
        });
        refreshButton.setTooltip(ToolTipFx.of("Refresh date/time to now()"));
        return refreshButton;
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
        updateDateTime();
        return hBox;
    }
}











