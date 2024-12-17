package com.example.databasebackup.util;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CsvUtil {

    public static void writeToCsv(String filePath, List<String[]> data) throws IOException {
        CSVWriterBuilder builder = new CSVWriterBuilder(new FileWriter(filePath));

        ICSVWriter writer = builder
                .build();

        writer.writeAll(data, false);

        writer.close();
    }

    public static void writeToCsv(String filePath, ResultSet resultSet) throws IOException, SQLException {
        ResultSetHelperService rsHelperService = new ResultSetHelperService();
        rsHelperService.setDateFormat("yyyy-MM-dd");

        CSVWriterBuilder builder = new CSVWriterBuilder(new FileWriter(filePath));

        ICSVWriter writer = builder
                .withResultSetHelper(rsHelperService)
                .build();

        writer.writeAll(resultSet, true, false, false);

        writer.close();
    }

    public static List<String[]> readFromCsv(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }

}
