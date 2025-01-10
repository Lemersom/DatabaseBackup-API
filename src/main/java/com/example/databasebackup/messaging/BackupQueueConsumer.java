package com.example.databasebackup.messaging;

import com.example.databasebackup.model.DatabaseConfigModel;
import com.example.databasebackup.service.BackupService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BackupQueueConsumer {

    private final BackupService backupService;

    public BackupQueueConsumer(BackupService backupService) {
        this.backupService = backupService;
    }

    @RabbitListener(queues = "${queue.backup.name}")
    public void receive(DatabaseConfigModel databaseConfig) {
        try {
            System.out.println("[BackupQueueConsumer] Processing backup request for database: " + databaseConfig.getDatabaseName());
            backupService.backupDatabase(databaseConfig);
        } catch (Exception e) {
            System.err.println("[BackupQueueConsumer] Failed to process backup for database: " + databaseConfig.getDatabaseName());
            e.printStackTrace();
        }
    }

}
