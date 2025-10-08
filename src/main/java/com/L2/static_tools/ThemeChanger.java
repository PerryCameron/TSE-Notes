package com.L2.static_tools;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.L2.BaseApplication;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.L2.BaseApplication.primaryStage;

public class ThemeChanger {
    private static final Logger logger = LoggerFactory.getLogger(ThemeChanger.class);
    public static void applyTheme() {
        BaseApplication.primaryStage.getScene().getStylesheets().clear();
        if (BaseApplication.theme.equals("light"))
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        else if (BaseApplication.theme.equals("dark"))
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        primaryStage.getScene().getStylesheets().add("css/" + BaseApplication.theme + ".css");
    }
}

