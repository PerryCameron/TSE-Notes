package com.L2.widgetFx;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class TextAreaFx {
    public static TextArea standardTextArea(boolean hGrow, double height, double fontSize, int prefRows) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setFont(Font.font(fontSize));
        textArea.setPrefHeight(height);
        HBox.setHgrow(textArea, Priority.ALWAYS);
        textArea.setPrefRowCount(prefRows); // Optional: Set a preferred number of rows
        textArea.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue)
                Platform.runLater(textArea::selectAll);
        });
        return textArea;
    }
}
