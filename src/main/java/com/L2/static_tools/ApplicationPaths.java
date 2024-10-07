package com.L2.static_tools;

import com.L2.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface ApplicationPaths {

    Path homeDir = Paths.get(System.getProperty("user.home"));
    Path oneDrive = homeDir.resolve("OneDrive - Schneider Electric");
    Path secondaryDbDirectory = homeDir.resolve("TSENotes");
    Path preferredDbDirectory = homeDir.resolve("OneDrive - Schneider Electric\\TSENotes");

}
