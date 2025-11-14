package com.L2.widgetFx;

import com.L2.static_tools.ImageResources;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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


    public static Node boundLabel(String title, StringProperty stringProperty) {
        HBox hbox = new HBox(5.0);
        hbox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("bound-label");

        TextField textField = new TextField();
        textField.setEditable(false); // Prevent editing
        textField.textProperty().bind(stringProperty); // make inline css to make textField look like a label
        textField.setPrefWidth(200);

        // Inline CSS to make TextField look like a Label
        textField.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-padding: 0;"
        );

        hbox.getChildren().addAll(titleLabel, textField);
        return hbox;
    }

    public static Node boundLabel(String title, LongProperty longProperty) {
        HBox hbox = new HBox(5.0);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("bound-label");
        Label textLabel = new Label();
        textLabel.textProperty().bind(longProperty.asString());
        hbox.getChildren().addAll(titleLabel, textLabel);
        return hbox;
    }

    public static Node boundLabel(String title, IntegerProperty integerProperty) {
        HBox hbox = new HBox(5.0);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("bound-label");
        Label textLabel = new Label();
        textLabel.textProperty().bind(integerProperty.asString());
        hbox.getChildren().addAll(titleLabel, textLabel);
        return hbox;
    }

    public static Node boundLabel(String title, DoubleProperty doubleProperty) {
        HBox hbox = new HBox(5.0);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("bound-label");
        Label textLabel = new Label();
        textLabel.textProperty().bind(doubleProperty.asString());
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
