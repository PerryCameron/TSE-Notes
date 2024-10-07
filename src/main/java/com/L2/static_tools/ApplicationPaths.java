package com.L2.static_tools;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface ApplicationPaths {

    Path homeDir = Paths.get(System.getProperty("user.home"));
    Path secondaryDbDirectory = homeDir.resolve("TSENotes");
    Path preferredDbDirectory = homeDir.resolve("OneDrive - Schneider Electric\\TSENotes");
}
