package com.L2.widgetFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class HBoxFx {

    public static HBox boundBoxOf(ObjectProperty<HBox> objectProperty) {
        HBox hBox = new HBox();
        objectProperty.bindBidirectional(new SimpleObjectProperty<>(hBox));
        return hBox;
    }
    public static HBox boundBoxOf(Insets insets, ObjectProperty<HBox> objectProperty) {
        HBox hBox = new HBox();
        hBox.setPadding(insets);
        objectProperty.bindBidirectional(new SimpleObjectProperty<>(hBox));
        return hBox;
    }
    public static HBox of(Pos alignment, Insets padding) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPadding(padding);
        return box;
    }

    public static HBox of(Pos alignment, Insets padding, boolean vGrow) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        if(vGrow) VBox.setVgrow(box, Priority.ALWAYS);
        box.setPadding(padding);
        return box;
    }

    public static HBox of(Insets padding, String id, boolean setVgrow) {
        HBox hBox = new HBox();
        hBox.setPadding(padding);
        hBox.setId(id);
        if(setVgrow) VBox.setVgrow(hBox, Priority.ALWAYS);
        return hBox;
    }

    public static HBox of(Pos alignment, double prefWidth) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        return box;
    }

    public static HBox of(Pos alignment, double prefWidth, Node node) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        box.getChildren().add(node);
        return box;
    }

    public static HBox of(Pos alignment, double prefWidth, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        box.setPrefWidth(prefWidth);
        return box;
    }

    public static HBox of(Pos alignment, double prefWidth, Insets padding) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        box.setPadding(padding);
        return box;
    }

    public static HBox of(Insets padding, Pos alignment, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        box.setPadding(padding);
        return box;
    }

    public static HBox of(double spacing, Pos alignment) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        return box;
    }

    public static HBox of(Pos alignment, double prefWidth, Insets padding, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        box.setPadding(padding);
        box.setSpacing(spacing);
        return box;
    }

    public static HBox of(Insets padding) {
        HBox box = new HBox();
        box.setPadding(padding);
        return box;
    }

    public static HBox of(Insets padding, double spacing) {
        HBox box = new HBox();
        box.setPadding(padding);
        box.setSpacing(spacing);
        return box;
    }

    public static HBox of(double spacing, Pos position, Node control1, Node control2) {
        HBox hBox = new HBox(spacing, control1,control2);
        hBox.setAlignment(position);
        return hBox;
    }

    public static HBox iconBox(double spacing) {
        HBox hBox = new HBox(spacing);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(0, 10, 0, 0));
        HBox.setHgrow(hBox, Priority.ALWAYS);
//        hBox.setStyle("-fx-background-color: #4d6955;");
        return hBox;
    }

    public static HBox testBox(String label) {
        HBox hBox = new HBox();
        Label testLabel = new Label(label);
        hBox.setPrefSize(500, 200);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-background-color: white;"); // Set background color to white
        hBox.getChildren().add(testLabel); // This should work as Label is a Node
        return hBox;
    }
}
