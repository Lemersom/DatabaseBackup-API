package com.example.backupvault.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.List;

public class CsvUtil {

    public static void writeToCsv(String filePath, List<String[]> data, String[] headers) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            if (headers != null) {
                writer.writeNext(headers);
            }
            for (String[] row : data) {
                writer.writeNext(row);
            }
        }
    }

    public static List<String[]> readFromCsv(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }
}
