package com.L2;

import com.L2.mvci_main.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseApplication extends Application {

    public static Stage primaryStage;

    public static void main(String[] args) { launch(args); }

    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);

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
        logAppVersion();
        primaryStage = stage;
        primaryStage.setTitle("Base Application");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(new MainController().getView()));
        primaryStage.show();
    }
}
