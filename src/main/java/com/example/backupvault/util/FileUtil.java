package com.example.backupvault.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static void createDirectoriesIfNotExists(String filePath) throws IOException {
        Path path = Paths.get(filePath).getParent();
        if (path != null) {
            Files.createDirectories(path);
        }
    }

}
