package com.L2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseApplication extends Application {

    public static Stage primaryStage;

    public static void main(String[] args) { launch(args); }

    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);


    @Override
    public void start(Stage stage) {
        logger.info("Starting Application");
        primaryStage = stage;
        primaryStage.setTitle("Base Application");
        primaryStage.setResizable(false);
        Button btn1 = new Button("Say, Hello World");
        Pane root=new Pane();
        HBox box=new HBox();
        box.getChildren().add(btn1);
        root.getChildren().add(box);
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("First JavaFX Application");
        primaryStage.show();
        primaryStage.show();
    }
}
