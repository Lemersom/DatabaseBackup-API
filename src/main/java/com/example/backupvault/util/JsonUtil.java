package com.example.backupvault.util;

import com.example.backupvault.model.TableDataModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void addRowsToJsonFile(String filePath, String tableName, List<JsonNode> newRows) throws IOException {
        File jsonFile = new File(filePath);
        TableDataModel currentData;

        if (jsonFile.exists() && jsonFile.length() > 0) {
            currentData = readFromJsonFile(filePath, TableDataModel.class);

            if (currentData.getTableName() == null || !currentData.getTableName().equals(tableName)) {
                System.out.println("The provided table name does not match the table name in the file.");
                return;
            }

            List<JsonNode> existingRows = currentData.getRows();
            existingRows.addAll(newRows);

            currentData = new TableDataModel(tableName, existingRows);

        } else {
            currentData = new TableDataModel(tableName, newRows);
        }

        writeToJsonFile(filePath, currentData);
    }

    public static <T> T readFromJsonFile(String filePath, Class<T> valueType) throws IOException {
        return objectMapper.readValue(new File(filePath), valueType);
    }

    public static void writeToJsonFile(String filePath, Object object) throws IOException {
        FileUtil.createDirectoriesIfNotExists(filePath);

        objectMapper.writeValue(new File(filePath), object);
    }

}
