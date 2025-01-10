package com.example.databasebackup.controller;

import com.example.databasebackup.dto.DatabaseConfigDTO;
import com.example.databasebackup.messaging.BackupQueueSender;
import com.example.databasebackup.model.DatabaseConfigModel;
import com.example.databasebackup.service.BackupService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/backup")
public class BackupController {

    private final BackupService backupService;
    private final BackupQueueSender backupQueueSender;

    public BackupController(BackupService backupService, BackupQueueSender backupQueueSender) {
        this.backupService = backupService;
        this.backupQueueSender = backupQueueSender;
    }

    @PostMapping
    public ResponseEntity<String> createBackupQueue(@RequestBody DatabaseConfigDTO dbConfigDTO) {
        DatabaseConfigModel dbConfig = new DatabaseConfigModel();
        BeanUtils.copyProperties(dbConfigDTO, dbConfig);
        try {
            backupQueueSender.send(dbConfig);
            return ResponseEntity.ok("Backup request sent for database: " + dbConfig.getDatabaseName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send backup request for database " + dbConfig.getDatabaseName() + " - Error: " + e.getMessage());
        }
    }

    @PostMapping("/no-queue")
    public ResponseEntity<String> createBackup(@RequestBody DatabaseConfigDTO dbConfigDTO) {
        DatabaseConfigModel dbConfig = new DatabaseConfigModel();
        BeanUtils.copyProperties(dbConfigDTO, dbConfig);
        try {
            backupService.backupDatabase(dbConfig);
            return ResponseEntity.ok("Backup successfully created for database: " + dbConfig.getDatabaseName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create backup for database: " + dbConfig.getDatabaseName() + " - Error: " + e.getMessage());
        }
    }

}
