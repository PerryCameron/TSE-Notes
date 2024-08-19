package com.L2.static_tools;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface ApplicationPaths {
//    String LOGFILEDIR = System.getProperty("user.home") + "/.gspares/logs";

    Path homeDir = Paths.get(System.getProperty("user.home"));
    Path settingsDir = homeDir.resolve("tsenotes");
    Path entitlementsFile = settingsDir.resolve("entitlements.settings");
}
