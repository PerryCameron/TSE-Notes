package com.L2.controls;


import atlantafx.base.theme.Styles;
import com.L2.mvci_main.MainView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.util.Builder;

import static com.L2.BaseApplication.primaryStage;


public class TitleBar implements Builder<Region> {
    private final MainView mainView;
    private double xOffset = 0;
    private double yOffset = 0;


    public TitleBar(MainView view) {
        this.mainView = view;
    }

    @Override
    public Region build() {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-background-color: #3a6684;"); // Set the background color
        hbox.setPadding(new Insets(0, 0, 0, 5));
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(40);


        // Load the image
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/AppList.targetsize-40_altform-unplated.png")));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        // Create the title label
        Label titleLabel = new Label("TSE Notes");
        titleLabel.getStyleClass().add(Styles.TITLE_4);
        titleLabel.setStyle("-fx-text-fill: white;");


        // Create an HBox to push the close button to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create the close button
        Button closeButton = new Button("X");
        closeButton.setOnAction(e -> primaryStage.close());


        // Handle dragging of the stage
        hbox.setOnMousePressed(event -> {
            xOffset = event.getScreenX() - primaryStage.getX();
            yOffset = event.getScreenY() - primaryStage.getY();
        });


        hbox.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });


        // Add the image, title, spacer, and close button to the title bar
        hbox.getChildren().addAll(imageView, titleLabel, spacer, createCloseButton());
        return hbox;
    }

    private Node createCloseButton() {
        // Create an HBox to contain the 'X'
        HBox closeButton = new HBox();
        closeButton.setPrefWidth(45);
        closeButton.setAlignment(Pos.CENTER); // Center the 'X' inside the HBox
        closeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");


        // Create the label for 'X'
        Label closeLabel = new Label("X");
        closeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16;");


        // Add the label to the HBox
        closeButton.getChildren().add(closeLabel);


        // Add hover effect to change background color to red
        closeButton.setOnMouseEntered(event -> closeButton.setStyle("-fx-background-color: red; -fx-cursor: hand;"));
        closeButton.setOnMouseExited(event -> closeButton.setStyle("-fx-background-color: transparent;"));


        // Set the action to close the stage when clicked
        closeButton.setOnMouseClicked(event -> primaryStage.close());


        return closeButton;
    }

}
