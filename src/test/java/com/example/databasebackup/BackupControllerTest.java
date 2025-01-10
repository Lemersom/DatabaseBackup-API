package com.example.databasebackup;

import com.example.databasebackup.dto.DatabaseConfigDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackupControllerTest {

    @Value("${backup.directory}")
    private String backupDirectory;

    @Autowired
    TestRestTemplate restTemplate;

    DatabaseConfigDTO testDbConfigDTO = new DatabaseConfigDTO(
            "postgres",
            "localhost",
            5435,
            "testdb",
            "jdbc:postgresql://localhost:5435/testdb",
            "postgres",
            "postgres"
    );

    @Test
    void shouldBackupSuccessfullyNoQueue() {
        ResponseEntity<String> response = restTemplate
                .postForEntity("/api/backup/no-queue", testDbConfigDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
