package com.L2.mvci_main.components;


import atlantafx.base.theme.Styles;
import com.L2.BaseApplication;
import com.L2.mvci_main.MainMessage;
import com.L2.mvci_main.MainView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.MenuFx;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.util.Builder;
import javafx.util.Duration;

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
        hbox.setMinHeight(40);


        // Load the image
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/TSELogo-24.png")));
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);


        Label titleLabel = null;
        // Create the title label
        if(BaseApplication.testMode)
            titleLabel = new Label("TSE Notes (Test Mode)");
        else
            titleLabel = new Label("TSE Notes");
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

    // the point of this is to hide a MenuBar until you click on the button.  I then want to be able to click on the menus and menu items.  I want the menu to go away and the button comes back if your mouse exists the menu or sub menus, or menu items
//    private Node menu() {
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER);
//        Button button = ButtonFx.utilityButton("/images/menu-24.png");
//        MenuBar menuBar = setUpMenuBar(hbox, button);
//        button.setOnAction(e -> { // this works great to show the menu
//            hbox.getChildren().remove(button);
//            hbox.getChildren().add(menuBar);
//        });
//        hbox.getChildren().addAll(button);
//        return hbox;
//    }

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
