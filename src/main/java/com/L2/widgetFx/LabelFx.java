package com.L2.widgetFx;

import com.L2.static_tools.ImageResources;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class LabelFx {

    public static Label of(String title) {
        Label label = new Label(title);
        label.setPadding(new Insets(0, 0, 0, 5));
        return label;
    }

    public static Node titledLabel(String title, String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("semi-prominent-label");
        Label textLabel = new Label(text);
        hbox.getChildren().addAll(titleLabel, textLabel);
        return hbox;
    }

    public static Node titledGraphicBoolean(String title, boolean value) {
        Image image = value ? ImageResources.NO : ImageResources.YES;
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("semi-prominent-label");
        hbox.getChildren().addAll(titleLabel, new ImageView(image));
        return hbox;
    }

    public static Node titledParagraph(String title, String text, double width) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("semi-prominent-label");
        Label textLabel = new Label(text);
        textLabel.setWrapText(true);
        vbox.setPrefWidth(width);
        vbox.getChildren().addAll(titleLabel, textLabel);
        return vbox;
    }
}
