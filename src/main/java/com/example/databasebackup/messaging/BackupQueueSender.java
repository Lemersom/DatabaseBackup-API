package com.example.databasebackup.messaging;

import com.example.databasebackup.model.DatabaseConfigModel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BackupQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue backupQueue;

    public BackupQueueSender(RabbitTemplate rabbitTemplate, Queue backupQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.backupQueue = backupQueue;
    }

    public void send(DatabaseConfigModel dbConfig) {
        rabbitTemplate.convertAndSend(this.backupQueue.getName(), dbConfig);
        System.out.println("[BackupQueueSender] Backup request queued for database: " + dbConfig.getDatabaseName());
    }

}
