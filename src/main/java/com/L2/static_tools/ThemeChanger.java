package com.L2.static_tools;


import com.L2.BaseApplication;
import javafx.application.Application;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;


public class ThemeChanger {
    private static final Logger logger = LoggerFactory.getLogger(ThemeChanger.class);

    //    public static void applyTheme() {
//        // Validate theme
//        String theme = BaseApplication.theme != null ? BaseApplication.theme : "light";
//        // if we cant find the correct theme let's default to light
//        if (!AppFileTools.getCssFileNames().contains(theme)) {
//            logger.warn("Invalid theme '{}'; defaulting to 'light'", theme);
//            theme = "light";
//            BaseApplication.theme = "light";
//        }
//
//        // Clear existing stylesheets and user agent stylesheet
//        BaseApplication.primaryStage.getScene().getStylesheets().clear();
//        Application.setUserAgentStylesheet(null);
//
//
//        URL cssUrl = ThemeChanger.class.getResource("/css/" + theme + ".css");
//
//        if (cssUrl != null) {
//            BaseApplication.primaryStage.getScene().getStylesheets().add(cssUrl.toExternalForm());
//            logger.info("Applied custom CSS: /css/{}.css", theme);
//        } else {
//            logger.warn("CSS file for theme '{}' not found; defaulting to 'light'", theme);
//            URL defaultCss = ThemeChanger.class.getResource("/css/light.css");
//            if (defaultCss != null) {
//                BaseApplication.primaryStage.getScene().getStylesheets().add(defaultCss.toExternalForm());
//                BaseApplication.theme = "light";
//                theme = "light";
//            } else {
//                logger.error("Default CSS file 'light.css' not found");
//            }
//        }
//    }
    public static void applyTheme() {
        // Validate theme
        String theme = BaseApplication.theme != null ? BaseApplication.theme : "light";
        if (!AppFileTools.getCssFileNames().contains(theme)) {
            logger.warn("Invalid theme '{}'; defaulting to 'light'", theme);
            theme = "light";
            BaseApplication.theme = "light";
        }

        // Get the scene
        Scene scene = BaseApplication.primaryStage.getScene();
        if (scene == null) {
            logger.error("Scene is null; cannot apply theme");
            return;
        }

        // Clear existing stylesheets and user agent stylesheet
        scene.getStylesheets().clear();
        Application.setUserAgentStylesheet(null);

        // Load the new stylesheet
        URL cssUrl = ThemeChanger.class.getResource("/css/" + theme + ".css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            logger.info("Applied custom CSS: /css/{}.css", theme);
        } else {
            logger.warn("CSS file for theme '{}' not found; defaulting to 'light'", theme);
            URL defaultCss = ThemeChanger.class.getResource("/css/light.css");
            if (defaultCss != null) {
                scene.getStylesheets().add(defaultCss.toExternalForm());
                BaseApplication.theme = "light";
                theme = "light";
            } else {
                logger.error("Default CSS file 'light.css' not found");
                return;
            }
        }

        // Force CSS reapplication
        scene.getRoot().applyCss();
        scene.getRoot().layout();
        logger.info("CSS reapplied for theme '{}'", theme);
    }
}

