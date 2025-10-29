package com.L2.mvci.main.components;


import com.L2.BaseApplication;
import com.L2.mvci.main.MainMessage;
import com.L2.mvci.main.MainView;
import com.L2.static_tools.ImageResources;
import com.L2.static_tools.VersionUtil;
import com.L2.widgetFx.DialogueFx;
import com.L2.widgetFx.MenuFx;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;

import static com.L2.BaseApplication.primaryStage;


public class TitleBar implements Builder<Region> {
    private final MainView mainView;
    private final BorderPane borderPane;
    private double xOffset = 0;
    private double yOffset = 0;

    public TitleBar(MainView view, BorderPane borderPane) {
        this.mainView = view;
        this.borderPane = borderPane;
    }

    @Override
    public Region build() {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("title-bar");
        hbox.setPadding(new Insets(0, 0, 0, 5));
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(40);
        hbox.setMinHeight(40);

        // Load the image
        ImageView imageView = new ImageView(ImageResources.TSELOGO24);
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);

        Label titleLabel;
        // Create the title label
        if(BaseApplication.testMode) {
//            hbox.setStyle("-fx-background-color: #5c1200;");
            hbox.getStyleClass().add("test-title-bar");
        }
        titleLabel = new Label("TSE Notes");
        titleLabel.getStyleClass().add("title-label");

        // Create an HBox to push the close button to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Handle dragging of the stage
        hbox.setOnMousePressed(event -> {
            if (!BaseApplication.isResizing) { // Only allow dragging if not resizing
                xOffset = event.getScreenX() - primaryStage.getX();
                yOffset = event.getScreenY() - primaryStage.getY();
            }
        });

        hbox.setOnMouseDragged(event -> {
            if (!BaseApplication.isResizing) { // Only allow dragging if not resizing
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        MenuBar menuBar = setUpMenuBar();
        Button menuButton = createMenuButton(menuBar);

        borderPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            // Only check if the MenuBar is visible
            if (menuBar.isVisible()) {
                // Get the bounds of the MenuBar and the Menu Button in scene coordinates
                Bounds menuBounds = menuBar.localToScene(menuBar.getBoundsInLocal());
                Bounds buttonBounds = menuButton.localToScene(menuButton.getBoundsInLocal());
                double x = event.getSceneX();
                double y = event.getSceneY();

                if (!menuBounds.contains(x, y) && !buttonBounds.contains(x, y)) {
                    menuBar.setVisible(false);
                }
            }
        });

        hbox.getChildren().addAll(imageView, titleLabel, menuButton, menuBar, spacer, createMinimizeButton(),
                createMaximizeButton(), createCloseButton());
        return hbox;
    }

    public Button createMenuButton(MenuBar menuBar) {
        // Define the properties for the lines
        double lineWidth = 15;
        double lineThickness = 1.5;
        double spacing = 3; // spacing between lines
        Color lineColor = Color.WHITE;

        // Create four horizontal lines
        Line line1 = new Line(0, 0, lineWidth, 0);
        line1.setStroke(lineColor);
        line1.setStrokeWidth(lineThickness);

        Line line2 = new Line(0, 0, lineWidth, 0);
        line2.setStroke(lineColor);
        line2.setStrokeWidth(lineThickness);

        Line line3 = new Line(0, 0, lineWidth, 0);
        line3.setStroke(lineColor);
        line3.setStrokeWidth(lineThickness);

        Line line4 = new Line(0, 0, lineWidth, 0);
        line4.setStroke(lineColor);
        line4.setStrokeWidth(lineThickness);

        // Arrange the lines vertically in a VBox with spacing
        VBox linesBox = new VBox(spacing, line1, line2, line3, line4);
        linesBox.setAlignment(Pos.CENTER);

        // Use a StackPane to center the VBox and set a fixed graphic size
        StackPane graphicPane = new StackPane(linesBox);
        graphicPane.setPrefSize(20, 20);

        // Create the button and set the graphic
        Button menuButton = new Button();
        menuButton.setGraphic(graphicPane);
        menuButton.getStyleClass().add("maximizeButton");

        // Optionally, add an action handler for the menu button
        menuButton.setOnAction(e -> {
            System.out.println("Menu button clicked");
            // TODO: Add your menu action here
        });
        menuButton.setOnAction(e -> {
            // Toggle the MenuBar to visible when button is clicked
            menuBar.setVisible(true);
        });

        return menuButton;
    }


    public Node createMinimizeButton() {
        // Create a single horizontal line for the minimize button
        Line line = new Line(0, 0, 10, 0); // Adjust the length as needed
        line.setStroke(Color.WHITE); // Set the color of the line
        line.setStrokeWidth(1); // Set the line thickness
        // Use a StackPane to center the line in the button
        StackPane linePane = new StackPane(line);
        linePane.setPrefSize(20, 20); // Adjust the size as needed
        // Create the button and set the StackPane as its graphic
        Button minimizeButton = new Button();
        minimizeButton.setGraphic(linePane);
        // Add hover effect
        minimizeButton.setOnAction(e -> primaryStage.setIconified(true));
        minimizeButton.getStyleClass().add("maximizeButton");
        // Return the button as a Node
        return minimizeButton;
    }


    public Node createMaximizeButton() {
        // Create a rectangle to represent the maximize button
        Rectangle square = new Rectangle(10, 10); // Adjust size as needed
        square.setStroke(Color.WHITE); // Outline color
        square.setFill(Color.TRANSPARENT); // Transparent fill
        square.setStrokeWidth(1); // Line width for the outline

        // Use a StackPane to center the rectangle in the button
        StackPane squarePane = new StackPane(square);
        squarePane.setPrefSize(20, 20); // Adjust size as needed

        // Create a button and set the StackPane as its graphic
        Button maximizeButton = new Button();
        maximizeButton.setGraphic(squarePane);
        maximizeButton.getStyleClass().add("maximizeButton");
        maximizeButton.setOnAction(e -> {
            primaryStage.setMaximized(!primaryStage.isMaximized()); // Restore to windowed mode
        });

        // Return the button as a Node
        return maximizeButton;
    }

    public Node createCloseButton() {
        // Create lines for the "X"
        Line line1 = new Line(0, 0, 10, 10);
        Line line2 = new Line(0, 10, 10, 0);

        // Style the lines
        line1.setStroke(Color.WHITE); // Change color as needed
        line1.setStrokeWidth(1);
        line2.setStroke(Color.WHITE); // Change color as needed
        line2.setStrokeWidth(1);

        // Use StackPane to hold the lines and create the "X" shape
        StackPane crossPane = new StackPane(line1, line2);
        crossPane.setPrefSize(20, 20); // Adjust the size as needed

        // Create the button and set the StackPane as its graphic
        Button closeButton = new Button();
        closeButton.setGraphic(crossPane);
        closeButton.getStyleClass().add("custom-close-button");

        // Set button action
        closeButton.setOnAction(e -> {
            mainView.getAction().accept(MainMessage.SHUTDOWN_EXECUTOR_SERVICE);
            primaryStage.close();
        });

        return closeButton;
    }

    private MenuBar setUpMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createFileMenu(), createDebugMenu(), createHelpMenu());
        menuBar.setVisible(false);
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");

        MenuItem close = MenuFx.menuItemOf("Settings", x -> mainView.getAction().accept(MainMessage.OPEN_SETTINGS), null);
        MenuItem changeSet = MenuFx.menuItemOf("Change Set", x -> mainView.getAction().accept(MainMessage.LAUNCH_CHANGE_SET_ALERT), null);
        menu.getItems().addAll(close, changeSet);
        return menu;
    }

    private Menu createDebugMenu() {
        Menu menu = new Menu("Debug");
        MenuItem showDebugLog = MenuFx.menuItemOf("Show Log", x -> mainView.getAction().accept(MainMessage.SHOW_LOG), null);
        if(BaseApplication.testMode) {
            MenuItem testDebug = MenuFx.menuItemOf("Test Ranges", x -> mainView.getAction().accept(MainMessage.PRINT_RANGES), null);
            MenuItem testDebug2 = MenuFx.menuItemOf("Print Selected Part Order Table", x -> mainView.getAction().accept(MainMessage.PRINT_PARTS), null);
            MenuItem testDebug3 = MenuFx.menuItemOf("Print product families", x -> mainView.getAction().accept(MainMessage.PRINT_PRODUCT_FAMILIES), null);
            menu.getItems().addAll(showDebugLog, testDebug, testDebug2, testDebug3);
        } else {
            menu.getItems().addAll(showDebugLog);
        }
        return menu;
    }

    private Menu createHelpMenu() {
        Menu menu = new Menu("Help");
        String message = "Version: " + VersionUtil.getVersion()
                + "\nBuilt: " + VersionUtil.getBuildTimestamp()
                + "\nBundled JDK: " + VersionUtil.getJavaVersion();
        MenuItem showAboutDialogue = MenuFx.menuItemOf("About", x -> {
            Alert alert = DialogueFx.aboutDialogue("TSE Notes", message, Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }, null);
        MenuItem getManual = MenuFx.menuItemOf("Manual", x -> mainView.getAction().accept(MainMessage.OPEN_MANUAL), null);
        menu.getItems().addAll(showAboutDialogue, getManual);
        return menu;
    }
}
