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
        System.out.println("[BackupService] Starting database backup for: " + dbConfig.getDatabaseName());
        Connection connection = null;

        String dateDirectory = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
        String databaseBackupDirectory = backupDirectory + File.separator + dbConfig.getDatabaseName() + File.separator + dateDirectory;

        try {
            connection = createConnection(dbConfig);
            connection.setAutoCommit(false); // Disable auto-commit for transactional integrity

            System.out.println("[BackupService] Creating backup directory: " + databaseBackupDirectory);
            Files.createDirectories(Paths.get(databaseBackupDirectory));

            List<String> tableNames = getTableNames(connection);
            if(tableNames.isEmpty()) {
                System.out.println("[BackupService] Tables not found.");
                System.out.println("[BackupService] Rolling back transaction for database: " + dbConfig.getDatabaseName());
                connection.rollback();
                cleanupBackupDirectory(databaseBackupDirectory);
            } else {
                for(String tableName : tableNames) {
                    backupTableData(connection, tableName, databaseBackupDirectory);
                    backupTableSchema(connection, tableName, databaseBackupDirectory);
                }

                connection.commit(); // Commit changes if everything is successful
                System.out.println("[BackupService] Backup completed for database: " + dbConfig.getDatabaseName());
            }

        } catch (Exception e) {
            System.err.println("[BackupService] Error occurred during database backup: " + e.getMessage());
            if(connection != null) {
                try {
                    System.out.println("[BackupService] Rolling back transaction for database: " + dbConfig.getDatabaseName());
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("[BackupService] Error during rollback: " + ex.getMessage());
                }
            }

            cleanupBackupDirectory(databaseBackupDirectory);
        } finally {
            closeConnection(connection);
        }

        return databaseBackupDirectory;
    }

    private void backupTableData(Connection connection, String tableName, String outputDir) throws SQLException, IOException {
        System.out.println("[BackupService] Exporting data for table: " + tableName);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        
        CsvUtil.writeToCsv(outputDir + File.separator + tableName + "_data.csv", resultSet);
        System.out.println("[BackupService] Successfully exported data for table: " + tableName);
    }

    private void backupTableSchema(Connection connection, String tableName, String outputDir) throws SQLException, IOException {
        System.out.println("[BackupService] Exporting schema for table: " + tableName);
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
        System.out.println("[BackupService] Successfully exported data for table: " + tableName);
    }

    private List<String> getTableNames(Connection connection) throws SQLException {
        System.out.println("[BackupService] Fetching table names...");
        List<String> tableNames = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            tableNames.add(tables.getString("TABLE_NAME"));
        }

        System.out.println("[BackupService] Table names fetched successfully.");
        return tableNames;
    }

    private Connection createConnection(DatabaseConfigModel dbConfig) throws SQLException {
        System.out.println("[BackupService] Connecting to database using URL: " + dbConfig.getConnectionUrl());
        return DriverManager.getConnection(dbConfig.getConnectionUrl(), dbConfig.getUsername(), dbConfig.getPassword());
    }

    private void closeConnection(Connection connection) {
        if(connection != null) {
            try {
                System.out.println("[BackupService] Closing database connection...");
                connection.close();
                System.out.println("[BackupService] Connection closed successfully.");
            } catch (SQLException ex) {
                System.err.println("[BackupService] Error while closing connection: " + ex.getMessage());
            }
        }
    }

    private void cleanupBackupDirectory(String dateBackupDirectory) {
        System.out.println("[BackupService] Cleaning up directory: " + dateBackupDirectory);
        File directory = new File(dateBackupDirectory);
        if(directory.exists()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    file.delete();
                }
            }
            directory.delete();
            System.out.println("[BackupService] Directory cleaned up successfully.");
        }
    }

}
