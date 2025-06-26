package com.L2.widgetFx;

import com.L2.static_tools.ImageResources;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Objects;

public class TitleBarFx {
    public static HBox of(String[] boxInfo, Runnable runnable) {
        HBox hBox = new HBox(5);
        Label label = LabelFx.of(boxInfo[0]);
        label.setPadding(new Insets(0, 0, 0, 5));
        HBox iconBox = HBoxFx.iconBox(10);
        ImageView imageViewCopy = new ImageView(ImageResources.COPY);
        Button copyButton = ButtonFx.of(imageViewCopy, "invisible-button");
        copyButton.setTooltip(ToolTipFx.of(boxInfo[1]));
        copyButton.setOnAction(e -> {
            runnable.run();
        });
        iconBox.getChildren().add(copyButton);
        hBox.getChildren().addAll(label, iconBox);
        return hBox;
    }

    public static HBox of(String boxInfo, Button[] buttons) {
        HBox hBox = new HBox(5);
        Label label = LabelFx.of(boxInfo);
        label.setPadding(new Insets(0, 0, 0, 5));
        HBox iconBox = HBoxFx.iconBox(10);
        for (Button button : buttons) {
            iconBox.getChildren().add(button);
        }
        hBox.getChildren().addAll(label, iconBox);
        return hBox;
    }

    public static HBox of(String boxInfo, HBox iconBox) {
        HBox hBox = new HBox(5);
        Label label = LabelFx.of(boxInfo);
        label.setPadding(new Insets(0, 0, 0, 5));
        hBox.getChildren().addAll(label, iconBox);
        return hBox;
    }
}
