package com.L2;

import atlantafx.base.theme.PrimerLight;
import com.L2.mvci_main.MainController;
import com.L2.static_tools.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseApplication extends Application {

    public static Stage primaryStage;
    public static boolean testMode = false;
    public static String dataBase = "notes.db";
    public static String dataBaseLocation = "";
    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    public static void main(String[] args) {
        try {
            AppFileTools.createFileIfNotExists(ApplicationPaths.secondaryDbDirectory);
            for (String arg : args) {
                if ("test".equalsIgnoreCase(arg)) {
                    testMode = true;
                    dataBase = "test-notes.db";
                }
            }
            if (!testMode) AppFileTools.startFileLogger();
            logger.info("TSENotes version {} Starting...", VersionUtil.getVersion());
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        // checks for the existence of the database we are going to use
        dataBaseLocation = StartUpManager.validateDatabase(dataBase);
            if (dataBaseLocation.equals("no-database")) {
                dataBaseLocation = ApplicationPaths.secondaryDbDirectory.toString();
                logger.info("setting dataBaseLocation: " + ApplicationPaths.secondaryDbDirectory.toString());
                SQLiteDatabaseCreator.createDataBase(ApplicationPaths.secondaryDbDirectory, dataBase);
            }
    }

    @Override
    public void start(Stage stage) {
            primaryStage = stage;
            primaryStage.setWidth(1028);
            primaryStage.setHeight(840);
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(800);
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(new MainController().getView()));
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            primaryStage.getScene().getStylesheets().add("css/light.css");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/TSELogo-64.png")));
            // Mouse pressed for dragging the window
            primaryStage.initStyle(StageStyle.UNDECORATED);
            WindowUtils.addResizeListeners(primaryStage);
            primaryStage.show();
    }
}
