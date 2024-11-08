package com.L2.static_tools;

import com.L2.BaseApplication;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowUtils {

    private static double xOffset = 0.0;
    private static double yOffset = 0.0;
    public static void addResizeListeners(Stage stage) {
        stage.getScene().setOnMousePressed(event -> {
            if (!BaseApplication.isResizing) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });


        stage.getScene().setOnMouseDragged(event -> {
            if (!BaseApplication.isResizing) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });


        stage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            double stageWidth = stage.getWidth();
            double stageHeight = stage.getHeight();
            double border = 10; // Distance from the edge to trigger resizing


            // Detect cursor position for resizing
            if (mouseX < border && mouseY < border) {
                stage.getScene().setCursor(javafx.scene.Cursor.NW_RESIZE); // Top-left corner
                BaseApplication.isResizing = true;
            } else if (mouseX > stageWidth - border && mouseY < border) {
                stage.getScene().setCursor(javafx.scene.Cursor.NE_RESIZE); // Top-right corner
                BaseApplication.isResizing = true;
            } else if (mouseX < border && mouseY > stageHeight - border) {
                stage.getScene().setCursor(javafx.scene.Cursor.SW_RESIZE); // Bottom-left corner
                BaseApplication.isResizing = true;
            } else if (mouseX > stageWidth - border && mouseY > stageHeight - border) {
                stage.getScene().setCursor(javafx.scene.Cursor.SE_RESIZE); // Bottom-right corner
                BaseApplication.isResizing = true;
            } else if (mouseX < border) {
                stage.getScene().setCursor(javafx.scene.Cursor.W_RESIZE); // Left edge
                BaseApplication.isResizing = true;
            } else if (mouseX > stageWidth - border) {
                stage.getScene().setCursor(javafx.scene.Cursor.E_RESIZE); // Right edge
                BaseApplication.isResizing = true;
            } else if (mouseY < border) {
                stage.getScene().setCursor(javafx.scene.Cursor.N_RESIZE); // Top edge
                BaseApplication.isResizing = true;
            } else if (mouseY > stageHeight - border) {
                stage.getScene().setCursor(javafx.scene.Cursor.S_RESIZE); // Bottom edge
                BaseApplication.isResizing = true;
            } else {
                stage.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
                BaseApplication.isResizing = false; // Exit resizing mode
            }
        });


        stage.getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (stage.getScene().getCursor() == javafx.scene.Cursor.E_RESIZE) {
                stage.setWidth(event.getX());
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.S_RESIZE) {
                stage.setHeight(event.getY());
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.W_RESIZE) {
                double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
                if (newWidth > stage.getMinWidth()) {
                    stage.setX(event.getScreenX());
                    stage.setWidth(newWidth);
                }
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.N_RESIZE) {
                double newHeight = stage.getHeight() - (event.getScreenY() - stage.getY());
                if (newHeight > stage.getMinHeight()) {
                    stage.setY(event.getScreenY());
                    stage.setHeight(newHeight);
                }
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.NW_RESIZE) {
                double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
                double newHeight = stage.getHeight() - (event.getScreenY() - stage.getY());
                if (newWidth > stage.getMinWidth()) {
                    stage.setX(event.getScreenX());
                    stage.setWidth(newWidth);
                }
                if (newHeight > stage.getMinHeight()) {
                    stage.setY(event.getScreenY());
                    stage.setHeight(newHeight);
                }
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.NE_RESIZE) {
                double newHeight = stage.getHeight() - (event.getScreenY() - stage.getY());
                if (newHeight > stage.getMinHeight()) {
                    stage.setY(event.getScreenY());
                    stage.setHeight(newHeight);
                }
                stage.setWidth(event.getX());
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.SW_RESIZE) {
                double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
                if (newWidth > stage.getMinWidth()) {
                    stage.setX(event.getScreenX());
                    stage.setWidth(newWidth);
                }
                stage.setHeight(event.getY());
            } else if (stage.getScene().getCursor() == javafx.scene.Cursor.SE_RESIZE) {
                stage.setWidth(event.getX());
                stage.setHeight(event.getY());
            }
        });
    }
}

