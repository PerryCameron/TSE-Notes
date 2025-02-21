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

import java.io.IOException;
import java.nio.file.Path;

public class BaseApplication extends Application {

    public static Stage primaryStage;
    public static boolean testMode = false;
    public static String dataBase = "notes.db";
    public static Path dataBaseLocation;
    public static boolean isResizing = false;
    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    public static void main(String[] args) {
        for (String arg : args) {
            if ("test".equalsIgnoreCase(arg)) {
                testMode = true;
                dataBase = "test-notes.db";
            }
        }
        // I prefer logs in the console in test mode
        if (!testMode) AppFileTools.startFileLogger();
        logger.info("  _______  _____    ______ ");
        logger.info(" |__   __ |  ___|  |  ____|");
        logger.info("    | |   | (___   | |__ ");
        logger.info("    | |    \\___ \\  |  __| ");
        logger.info("    | |    ____) ) | |____");
        logger.info("    |_|   |_____/  |______|");
        logger.info("    Notes version {} Starting...", VersionUtil.getVersion());
        launch(args);
    }

    @Override
    public void init() throws IOException {
        // create /TSCNotes in user home if not already there (first time launch)
        AppFileTools.createFileIfNotExists(ApplicationPaths.secondaryDbDirectory);
        // checks for one drive path, if no one-drive path, default to user home
        dataBaseLocation = AppFileTools.getDbPath();
        // if a database can not be found at preferred path or back-up (first time launch) , then create one.
        if (!BaseApplication.dataBaseLocation.resolve(dataBase).toFile().exists()) {
            // this will create the schema, but also will populate a few rows needed for application.
            SQLiteDatabaseCreator.createDataBase(dataBase);
        } else {
            // the database already exits, lets check that it is up to date
            DatabaseTools.backupDatabase();
            DatabaseTools.checkForDatabaseChanges();
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
