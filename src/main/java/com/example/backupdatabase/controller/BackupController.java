package com.example.backupdatabase.controller;

import com.example.backupdatabase.model.DatabaseConfigModel;
import com.example.backupdatabase.service.BackupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping
    public ResponseEntity<String> createBackup(@RequestBody DatabaseConfigModel dbConfig) {
        try {
            backupService.backupDatabase(dbConfig);
            return ResponseEntity.ok("Backup successfully created for database: " + dbConfig.getDatabaseName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create backup for database: " + dbConfig.getDatabaseName() + " - Error: " + e.getMessage());
        }
    }

}
