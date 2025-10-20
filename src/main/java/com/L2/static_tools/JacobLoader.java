package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JacobLoader {
    private static final Logger logger = LoggerFactory.getLogger(JacobLoader.class);

    public static void initJacob() {
        String dllPathInJar = "/dll/jacob-1.21-x64.dll"; // Path in resources
        try {
            long startTime = System.nanoTime();
            File tempDll = extractNativeLibrary(dllPathInJar);
            logger.debug("Extracted jacob-1.21-x64.dll to: {}", tempDll.getAbsolutePath());

            // Set the Jacob property to point to the extracted DLL
            System.setProperty("jacob.dll.path", tempDll.getAbsolutePath());
            logger.debug("Jacob DLL path set for loading: {}", tempDll.getAbsolutePath());
            logger.info("Jacob DLL setup completed in {} ms", (System.nanoTime() - startTime) / 1_000_000.0);

            // Optional: Test Jacob initialization
            try {
                new com.jacob.activeX.ActiveXComponent("Outlook.Application");
                logger.info("Jacob initialized successfully with Outlook");
            } catch (Exception e) {
                logger.error("Failed to initialize Jacob with Outlook", e);
                throw new RuntimeException("Jacob COM initialization failed", e);
            }
        } catch (IOException e) {
            logger.error("Failed to extract Jacob DLL", e);
            throw new RuntimeException("Failed to initialize Jacob DLL", e);
        }
    }

    // Your exact extractNativeLibrary method
    private static File extractNativeLibrary(String resourcePath) throws IOException {
        InputStream in = JacobLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IOException("Resource not found in JAR: " + resourcePath);
        }

        File tempFile = File.createTempFile("jacob-", ".dll"); // Adjusted prefix for Jacob
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            in.close();
        }
        return tempFile;
    }
}
