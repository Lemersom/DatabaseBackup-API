package com.example.backupvault.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readFromJsonFile(String filePath, Class<T> valueType) throws IOException {
        return objectMapper.readValue(new File(filePath), valueType);
    }

    public static void writeToJsonFile(String filePath, Object object) throws IOException {
        FileUtil.createDirectoriesIfNotExists(filePath);

        objectMapper.writeValue(new File(filePath), object);
    }

}
