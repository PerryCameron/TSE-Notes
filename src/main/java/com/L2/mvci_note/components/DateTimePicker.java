package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteView;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimePicker implements Builder<Region> {

    private final NoteView noteView;
    private final DatePicker datePicker;
    private final Spinner<Integer> hourSpinner;
    private final Spinner<Integer> minuteSpinner;


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
        HBox hBox = new HBox();

        // Configure the spinners
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));

        hourSpinner.setPrefWidth(80);
        minuteSpinner.setPrefWidth(80);
        datePicker.setPrefWidth(150);

        // Label for separating hours and minutes
        Label colonLabel = new Label(":");

        Button button = new Button("Stamp Now");
        // Add components to the HBox
        hBox.getChildren().addAll(datePicker, hourSpinner, colonLabel, minuteSpinner, button);
        hBox.setSpacing(10);

        // Bind the dateTimeProperty to the current selection
        datePicker.setOnAction(event -> updateDateTime());
        hourSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDateTime());
        minuteSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDateTime());
        button.setOnAction(event -> {
            noteView.getNoteModel().setStatusLabel("Time Stamped!");
            setDateTime(LocalDateTime.now());
        });
        // Initialize with the current date and time
        updateDateTime();
        return hBox;
    }
}











