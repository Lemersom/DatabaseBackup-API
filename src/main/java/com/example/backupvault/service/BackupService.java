package com.example.backupvault.service;

import com.example.backupvault.model.DatabaseConfigModel;
import com.example.backupvault.util.CsvUtil;
import com.example.backupvault.util.FileUtil;
import com.example.backupvault.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackupService {

    @Value("${backup.directory}")
    private String backupDirectory;

    public String backupDatabase(DatabaseConfigModel dbConfig) {
        Connection connection = null;

        String dateDirectory = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
        String databaseBackupDirectory = backupDirectory + File.separator + dbConfig.getDatabaseName() + File.separator + dateDirectory;

        try {
            connection = createConnection(dbConfig);
            connection.setAutoCommit(false);

            FileUtil.createDirectoriesIfNotExists(databaseBackupDirectory);

            List<String> tables = getTableNames(connection);

            for(String tableName : tables) {
                // Data
                List<Map<String, Object>> tableData = getTableData(connection, tableName);
                String dataFilePath = databaseBackupDirectory + File.separator + tableName + "-data";
                JsonUtil.writeToJsonFile(dataFilePath + ".json", tableData);
                CsvUtil.writeDataToCsvFile(dataFilePath + ".csv", tableData);

                // Schema
                Map<String, String> tableSchema = getTableSchema(connection, tableName);
                String schemaFilePath = databaseBackupDirectory + File.separator + tableName + "-schema";
                JsonUtil.writeToJsonFile(schemaFilePath + ".json", tableSchema);
                CsvUtil.writeSchemaToCsvFile(schemaFilePath + ".csv", tableSchema);
            }

            connection.commit();

        } catch (Exception e) {
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.err.println("Error on backup: " + e.getMessage());
            cleanupBackupDirectory(databaseBackupDirectory);
        } finally {
            closeConnection(connection);
        }

        return databaseBackupDirectory;
    }

    private Connection createConnection(DatabaseConfigModel dbConfig) throws SQLException {
        return DriverManager.getConnection(dbConfig.getConnectionUrl(), dbConfig.getUsername(), dbConfig.getPassword());
    }

    private void closeConnection(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<String> getTableNames(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while(resultSet.next()) {
            tables.add(resultSet.getString("TABLE_NAME"));
        }

        return tables;
    }

    private List<Map<String, Object>> getTableData(Connection connection, String tableName) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for(int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                rows.add(row);
            }
        }

        return rows;
    }

    private Map<String, String> getTableSchema(Connection connection, String tableName) throws SQLException {
        Map<String, String> columnTypes = new HashMap<>();

        String query = "SELECT * FROM " + tableName + " LIMIT 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnType = metaData.getColumnTypeName(i);
                columnTypes.put(columnName, columnType);
            }
        }

        return columnTypes;
    }

    private List<Map<String, Object>> getTableDataWithTypes(Connection connection, String tableName) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, String> columnTypes = getTableSchema(connection, tableName);
        String query = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnType = columnTypes.get(columnName);
                    Object columnValue = resultSet.getObject(i);

                    Map<String, Object> valueWithType = new HashMap<>();
                    valueWithType.put("value", columnValue);
                    valueWithType.put("type", columnType);
                    row.put(columnName, valueWithType);
                }
                rows.add(row);
            }
        }

        return rows;
    }

    private void cleanupBackupDirectory(String dateBackupDirectory) {
        File directory = new File(dateBackupDirectory);
        if(directory.exists()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    file.delete();
                }
            }
            directory.delete();
        }
    }

}
