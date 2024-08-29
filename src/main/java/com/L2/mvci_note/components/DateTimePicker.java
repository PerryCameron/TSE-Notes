package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.VBoxFx;
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
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
        vBox.getStyleClass().add("decorative-hbox");
        Button copyButton = new Button();
        Image copyIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/copy-16.png")));
        ImageView imageViewCopy = new ImageView(copyIcon);
        copyButton.setGraphic(imageViewCopy);
        copyButton.getStyleClass().add("invisible-button");
        copyButton.setOnAction(e -> {
//            noteModel.getCurrentNote().setSelectedPartOrder(partOrderDTO);
//            noteView.getAction().accept(NoteMessage.COPY_PART_ORDER);
            // Apply a blue border to the VBox
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 2px; -fx-border-radius: 5px");
            // Use a PauseTransition to remove the border after 0.5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
            noteView.getAction().accept(NoteMessage.COPY_NAME_DATE);
        });
        HBox hBox = HBoxFx.of(Pos.CENTER_RIGHT, new Insets(0, 5, 0, 0));
        hBox.getChildren().add(copyButton);
        vBox.getChildren().addAll(hBox ,dateTimePicker());
        return vBox;
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

        Button button = new Button("Stamp Now");
        // Add components to the HBox
        hBox.getChildren().addAll(datePicker, hourSpinner, colonLabel, minuteSpinner, button);

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











