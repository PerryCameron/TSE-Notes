package com.L2.widgetFx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

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
        button.getStyleClass().add("app-button");
        button.setPrefWidth(width);
        button.setToggleGroup(tg);
        return button;
    }

    public static Button of(ImageView image, String cssClass) {
        Button button = new Button();
        button.setGraphic(image);
        button.getStyleClass().add(cssClass);
        return button;
    }

    public static Button utilityButton(Runnable runnable, Image image, String... strings) {
        ImageView imageViewCopy = new ImageView(image);
        Button button = of(imageViewCopy, "invisible-button");
        button.setText(strings[0]);
        button.setOnAction(event -> runnable.run());
        return button;
    }

    public static Button utilityButton(Image image, Runnable runnable) {
        ImageView imageViewCopy = new ImageView(image);
        Button button = of(imageViewCopy, "invisible-button");
        button.setOnAction(event -> runnable.run());
        return button;
    }

    public static Button utilityButton(Image image) {
        ImageView imageViewCopy = new ImageView(image);
        Button button = of(imageViewCopy, "invisible-button");
        return button;
    }

    public static Button utilityButton(Image image, String text, double width) {
        ImageView imageViewCopy = new ImageView(image);
        Button button = ButtonFx.of(imageViewCopy, "invisible-button");
        button.setText(text);
        button.setPrefWidth(width);
        button.setContentDisplay(ContentDisplay.LEFT); // Image and text side-by-side, left-aligned
        button.setAlignment(Pos.CENTER_LEFT); // Align content to the left within the button
        button.setGraphicTextGap(5.0); // Optional: Adjust gap between image and text
        return button;
    }

    public static void buttonVisible(Button button, boolean value) {
        button.setVisible(value);
        button.setManaged(value);
    }
}
