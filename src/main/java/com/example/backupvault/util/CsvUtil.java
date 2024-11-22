package com.example.backupvault.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CsvUtil {

    public static void writeDataToCsvFile(String filePath, List<Map<String, Object>> data) throws IOException {
        if(data.isEmpty()) {
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            Set<String> headers = data.get(0).keySet();
            writer.writeNext(headers.toArray(new String[0]));

            for(Map<String, Object> row : data) {
                String[] rowData = headers.stream()
                        .map(header -> row.getOrDefault(header, "").toString())
                        .toArray(String[]::new);
                writer.writeNext(rowData);
            }
        }
    }

    public static List<Map<String, Object>> readDataFromCsvFile(String filePath) throws IOException, CsvException {
        List<Map<String, Object>> data = new ArrayList<>();

        try(CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            if(rows.isEmpty()) {
                return data;
            }

            String[] headers = rows.get(0);

            for(int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
                String[] row = rows.get(rowIndex);
                Map<String, Object> rowData = new HashMap<>();
                for(int columnIndex = 0; columnIndex < headers.length; columnIndex++) {
                    rowData.put(headers[columnIndex], row[columnIndex]);
                }
                data.add(rowData);
            }
        }

        return data;
    }

    public static void writeSchemaToCsvFile(String filePath, Map<String, String> schema) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : schema.entrySet()) {
                writer.writeNext(new String[]{entry.getKey(), entry.getValue()});
            }
        }
    }

    public static Map<String, String> readSchemaFromCsvFile(String filePath) throws IOException, CsvException {
        Map<String, String> schema = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            if(rows.isEmpty()) {
                return schema;
            }

            for (String[] row : rows) {
                if (row.length == 2) {
                    schema.put(row[0], row[1]);
                }
            }
        }

        return schema;
    }

}
