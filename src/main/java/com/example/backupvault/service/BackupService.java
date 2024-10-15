package com.example.backupvault.service;

import com.example.backupvault.model.DatabaseConfigModel;
import com.example.backupvault.util.FileUtil;
import com.example.backupvault.util.JsonUtil;
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

    public void backupDatabase(DatabaseConfigModel dbConfig, String backupDirectory) {
        Connection connection = null;
        try {
            connection = createConnection(dbConfig);
            connection.setAutoCommit(false);

            List<String> tables = getTableNames(connection);

            String databaseBackupDirectory = backupDirectory + File.separator + dbConfig.getDatabaseName();
            FileUtil.createDirectoriesIfNotExists(databaseBackupDirectory);

            for(String tableName : tables) {
                List<Map<String,Object>> tableData = getTableData(connection, tableName);
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
                String filePath = databaseBackupDirectory + File.separator + tableName + "-" + timestamp + ".json";
                JsonUtil.writeToJsonFile(filePath, tableData);
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
            cleanupBackupDirectory(backupDirectory);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private Connection createConnection(DatabaseConfigModel dbConfig) throws SQLException {
        // TODO: Class.forName may be needed to load driver

        String url = dbConfig.getConnectionUrl();

        return DriverManager.getConnection(url, dbConfig.getUsername(), dbConfig.getPassword());
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

    private void cleanupBackupDirectory(String backupDir) {
        File directory = new File(backupDir);
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
