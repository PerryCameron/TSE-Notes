package com.L2.static_tools;


import com.L2.BaseApplication;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class ThemeChanger {
    private static final Logger logger = LoggerFactory.getLogger(ThemeChanger.class);

    public static void applyTheme() {
        // Validate theme
        String theme = BaseApplication.theme != null ? BaseApplication.theme : "light";
        if (!AppFileTools.getCssFileNames().contains(theme)) {
            logger.warn("Invalid theme '{}'; defaulting to 'light'", theme);
            theme = "light";
            BaseApplication.theme = "light";
        }

        // Clear existing stylesheets and user agent stylesheet
        BaseApplication.primaryStage.getScene().getStylesheets().clear();
        Application.setUserAgentStylesheet(null);

        // Apply AtlantaFX user agent stylesheet
        try {
//            if (theme.equals("light")) {
//                Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
//            } else if (theme.equals("dark")) {
//                //Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
//            }
            logger.info("Applied AtlantaFX theme: {}", theme);
        } catch (Exception e) {
            logger.error("Failed to apply AtlantaFX theme '{}'; defaulting to Modena", theme, e);
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            theme = "light";
            BaseApplication.theme = "light";
        }

        // Add custom CSS to override AtlantaFX issues
        URL cssUrl = ThemeChanger.class.getResource("/css/" + theme + ".css");
        if (cssUrl != null) {
            BaseApplication.primaryStage.getScene().getStylesheets().add(cssUrl.toExternalForm());
            logger.info("Applied custom CSS: /css/{}.css", theme);
        } else {
            logger.warn("CSS file for theme '{}' not found; defaulting to 'light'", theme);
            URL defaultCss = ThemeChanger.class.getResource("/css/light.css");
            if (defaultCss != null) {
                BaseApplication.primaryStage.getScene().getStylesheets().add(defaultCss.toExternalForm());
                BaseApplication.theme = "light";
                theme = "light";
            } else {
                logger.error("Default CSS file 'light.css' not found");
            }
        }
    }
}

