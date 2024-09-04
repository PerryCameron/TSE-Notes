package com.L2.widgetFx;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class TitleBarFx {
    public static HBox of(Runnable runnable) {
        HBox hBox = new HBox(5);
        Label label = LabelFx.of("Shipping Information");
        label.setPadding(new Insets(0, 0, 0, 5));
        HBox iconBox = HBoxFx.iconBox();
        Image copyIcon = new Image(Objects.requireNonNull(TitleBarFx.class.getResourceAsStream("/images/copy-16.png")));
        ImageView imageViewCopy = new ImageView(copyIcon);
        Button copyButton = ButtonFx.of(imageViewCopy, "invisible-button");
        copyButton.setTooltip(ToolTipFx.of("Copy shipping contact / address"));
        copyButton.setOnAction(e -> {
            runnable.run();
        });
        iconBox.getChildren().add(copyButton);
        hBox.getChildren().addAll(label, iconBox);
        return hBox;
    }
}
