package com.L2.widgetFx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HeaderFx {
    public static Node withTitle(String message) {
        VBox vBox = VBoxFx.of(true,5.0, new Insets(10, 5, 10, 5));
        vBox.getStyleClass().add("decorative-header-box");
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(new Label(message));
        VBox.setMargin(vBox, new Insets(10, 0, 10, 0));
        return vBox;
    }
}
