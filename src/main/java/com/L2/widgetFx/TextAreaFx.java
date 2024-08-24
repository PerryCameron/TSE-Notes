package com.L2.widgetFx;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class TextAreaFx {
    public static TextArea of(boolean hGrow, double height, double fontSize, int prefRows) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setFont(Font.font(fontSize));
        textArea.setPrefHeight(height);
        HBox.setHgrow(textArea, Priority.ALWAYS);
        textArea.setPrefRowCount(prefRows); // Optional: Set a preferred number of rows
        return textArea;
    }
}
