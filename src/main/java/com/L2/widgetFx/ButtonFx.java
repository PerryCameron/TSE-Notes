package com.L2.widgetFx;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class ButtonFx {
    public static Button bigButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(70);
        button.setId("big-button");
        return button;
    }

    public static Button of(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        return button;
    }

    public static Button of(String text, double width, Runnable runnable) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setOnAction(event -> runnable.run());
        return button;
    }

    public static ToggleButton toggleof(String text, double width, ToggleGroup tg) {
        ToggleButton button = new ToggleButton(text);
        button.setPrefWidth(width);
        button.setToggleGroup(tg);
        return button;
    }
}
