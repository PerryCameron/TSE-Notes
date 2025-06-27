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
    Path backupDir = Paths.get(secondaryDbDirectory.toString(), "backup");
    Path globalSparesDir = Paths.get(secondaryDbDirectory.toString(), "global_spares");
    Path changeSetDir = Paths.get(System.getProperty("user.home") + "\\TSENotes", "change_sets");
    // this will be where we store attachments
    Path fileDirectory = homeDir.resolve("OneDrive - Schneider Electric\\TSENotes\\files");


}
