package com.L2.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class DateTimePicker extends HBox {

    private final DatePicker datePicker;
    private final Spinner<Integer> hourSpinner;
    private final Spinner<Integer> minuteSpinner;

    private final ObjectProperty<LocalDateTime> dateTimeProperty;


    public DateTimePicker() {
        // Initialize components
        datePicker = new DatePicker(LocalDate.now());
        hourSpinner = new Spinner<>();
        minuteSpinner = new Spinner<>();
        dateTimeProperty = new SimpleObjectProperty<>();


        // Configure the spinners
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));

        hourSpinner.setPrefWidth(80);
        minuteSpinner.setPrefWidth(80);
        datePicker.setPrefWidth(150);


        // Label for separating hours and minutes
        Label colonLabel = new Label(":");


        // Add components to the HBox
        this.getChildren().addAll(datePicker, hourSpinner, colonLabel, minuteSpinner);
        this.setSpacing(10);


        // Bind the dateTimeProperty to the current selection
        datePicker.setOnAction(event -> updateDateTime());
        hourSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDateTime());
        minuteSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDateTime());


        // Initialize with the current date and time
        updateDateTime();
    }


    private void updateDateTime() {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
        dateTimeProperty.set(LocalDateTime.of(date, time));
    }


    public ObjectProperty<LocalDateTime> dateTimeProperty() {
        return dateTimeProperty;
    }


    public LocalDateTime getDateTime() {
        return dateTimeProperty.get();
    }


    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            datePicker.setValue(dateTime.toLocalDate());
            hourSpinner.getValueFactory().setValue(dateTime.getHour());
            minuteSpinner.getValueFactory().setValue(dateTime.getMinute());
        }
    }

    // Example method to get the formatted DateTime as a String
    public String getFormattedDateTime() {
        return getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}











