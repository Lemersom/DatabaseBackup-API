package com.example.backupvault;

import com.example.backupvault.model.DatabaseConfigModel;
import com.example.backupvault.service.BackupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BackupServiceTest {

    @Autowired
    BackupService backupService;

    @Value("${backup.directory}")
    private String backupDirectory;

    @Test
    public void shouldBackupSuccessfully() {
        DatabaseConfigModel dbConfig = new DatabaseConfigModel();
        dbConfig.setCustomUrl("jdbc:postgresql://localhost:5435/testdb");
        dbConfig.setDatabaseName("testdb");
        dbConfig.setUsername("postgres");
        dbConfig.setPassword("postgres");

        try {
            String backupCreatedDirectory = backupService.backupDatabase(dbConfig);

            File backupDir = new File(backupCreatedDirectory);
            assertTrue(backupDir.exists() && backupDir.list().length > 0, "Backup directory exists and contains files");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Backup should not throw exceptions");
        }
    }

}
