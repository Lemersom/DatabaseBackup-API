package com.example.databasebackup.service;

import com.example.databasebackup.model.DatabaseConfigModel;
import com.example.databasebackup.util.CsvUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
            connection.setAutoCommit(false); // Disable auto-commit for transactional integrity

            Files.createDirectories(Paths.get(databaseBackupDirectory));

            List<String> tableNames = getTableNames(connection);

            for(String tableName : tableNames) {
                // Data
                backupTableData(connection, tableName, databaseBackupDirectory);

                // Schema
                backupTableSchema(connection, tableName, databaseBackupDirectory);
            }

            connection.commit(); // Commit changes if everything is successful

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

    private void backupTableData(Connection connection, String tableName, String outputDir) throws SQLException, IOException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        
        CsvUtil.writeToCsv(outputDir + File.separator + tableName + "_data.csv", resultSet);
    }

    private void backupTableSchema(Connection connection, String tableName, String outputDir) throws SQLException, IOException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);

        List<String[]> schema = new ArrayList<>();
        schema.add(new String[]{"column_name", "data_type"});
        while(columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String dataType = columns.getString("TYPE_NAME");
            schema.add(new String[]{columnName, dataType});
        }

        CsvUtil.writeToCsv(outputDir + File.separator + tableName + "_schema.csv", schema);
    }

    private List<String> getTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            tableNames.add(tables.getString("TABLE_NAME"));
        }

        return tableNames;
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
