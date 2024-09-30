package com.L2;

import atlantafx.base.theme.PrimerLight;
import com.L2.mvci_main.MainController;
import com.L2.static_tools.AppFileTools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseApplication extends Application {

    public static Stage primaryStage;
    public static boolean testMode = false;

    public static void main(String[] args) {
//        AppFileTools.startFileLogger();
        logger.info("TSENotes version 1.0 Starting...");
        for (String arg : args) {
            if ("test".equalsIgnoreCase(arg)) {
                testMode = true;
                System.out.println("Running in test mode.");
            }
        }
        launch(args);
    }

    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    private double xOffset = 0.0;
    private double yOffset = 0.0;

    private boolean isResizing = false; // Flag to indicate whether resizing is active

    private static String logAppVersion() {
        Properties properties = new Properties();
        try (InputStream input = BaseApplication.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                logger.error("Sorry, unable to find app.properties");
                return "unknown" ;
            }
            properties.load(input);
            String appVersion = properties.getProperty("app.version");
            logger.info("Starting GlobalSpares Application version: " + appVersion);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties.getProperty("app.version");
    }

    @Override
    public void start(Stage stage) {
//        logAppVersion();
        try {
            primaryStage = stage;
            primaryStage.setWidth(1028);
            primaryStage.setHeight(840);
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(800);
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(new MainController().getView()));
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            primaryStage.getScene().getStylesheets().add("css/dark/light.css");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/TSELogo-64.png")));
            // Mouse pressed for dragging the window
            primaryStage.getScene().setOnMousePressed(event -> {
                if (!isResizing) { // Only allow moving if not resizing
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            // Mouse dragged for dragging the window
            primaryStage.getScene().setOnMouseDragged(event -> {
                if (!isResizing) { // Only allow moving if not resizing
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            primaryStage.initStyle(StageStyle.UNDECORATED);
            addResizeListeners(primaryStage, primaryStage.getScene());
            primaryStage.show();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void addResizeListeners(Stage stage, Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            if (mouseX > stage.getWidth() - 10) {
                scene.setCursor(javafx.scene.Cursor.H_RESIZE);
                isResizing = true; // Enter resizing mode
            } else if (mouseY > stage.getHeight() - 10) {
                scene.setCursor(javafx.scene.Cursor.V_RESIZE);
                isResizing = true; // Enter resizing mode
            } else {
                scene.setCursor(javafx.scene.Cursor.DEFAULT);
                isResizing = false; // Exit resizing mode
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (scene.getCursor() == javafx.scene.Cursor.H_RESIZE) {
                stage.setWidth(event.getX());
            } else if (scene.getCursor() == javafx.scene.Cursor.V_RESIZE) {
                stage.setHeight(event.getY());
            }
        });
    }
}
