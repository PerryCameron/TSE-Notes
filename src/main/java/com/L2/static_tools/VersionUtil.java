package com.L2.static_tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

    private static final String VERSION_FILE = "/version.properties";

    public static String getVersion() {
        String version = getProperty("version", "Unknown");
        // Remove leading "v"
        if (version.startsWith("v")) {
            version = version.substring(1);
        }

        // Remove commit hash (-gxxxxxxx)
        int commitHashIndex = version.indexOf("-g");
        if (commitHashIndex != -1) {
            version = version.substring(0, commitHashIndex);
        }
        return version;
    }

    public static String getBuildTimestamp() {
        return getProperty("build.timestamp", "Unknown");
    }

    public static String getJavaVersion() {
        return getProperty("java.version", "Unknown");
    }

    private static String getProperty(String key, String defaultValue) {
        Properties props = new Properties();
        try (InputStream input = VersionUtil.class.getResourceAsStream(VERSION_FILE)) {
            if (input == null) {
                return defaultValue;
            }
            props.load(input);
            return props.getProperty(key, defaultValue);
        } catch (IOException ex) {
            ex.printStackTrace();
            return defaultValue;
        }
    }

}

