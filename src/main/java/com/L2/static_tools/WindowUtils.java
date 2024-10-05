package com.L2.static_tools;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowUtils {
    private static boolean isResizing = false;
    private static double xOffset = 0.0;
    private static double yOffset = 0.0;
    public static void addResizeListeners(Stage stage) {

        stage.getScene().setOnMousePressed(event -> {
            if (!isResizing) { // Only allow moving if not resizing
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        // Mouse dragged for dragging the window
        stage.getScene().setOnMouseDragged(event -> {
            if (!isResizing) { // Only allow moving if not resizing
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            if (mouseX > stage.getWidth() - 10) {
                stage.getScene().setCursor(javafx.scene.Cursor.H_RESIZE);
                isResizing = true; // Enter resizing mode
            } else if (mouseY > stage.getHeight() - 10) {
                stage.getScene().setCursor(javafx.scene.Cursor.V_RESIZE);
                isResizing = true; // Enter resizing mode
            } else {
                stage.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
                isResizing = true; // Exit resizing mode
            }
        });

        stage.getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (stage.getScene().getCursor() == javafx.scene.Cursor.H_RESIZE) {
                stage.setWidth(event.getX());
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.V_RESIZE) {
                stage.setHeight(event.getY());
            }
        });
    }
}

