package com.L2.services;

import com.L2.static_tools.ApplicationPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class FileStorageService {



    public String saveFile(byte[] fileData, String originalFilename) throws IOException {
        // Generate a unique file name (here we use a UUID)
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + extension;


        Path filePath = Paths.get(String.valueOf(ApplicationPaths.fileDirectory), uniqueFilename);
        Files.write(filePath, fileData);


        // Save uniqueFilename in the database along with other metadata


        return uniqueFilename;
    }
}


