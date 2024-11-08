package com.example.backupvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatabaseConfigDTO(
        @NotBlank(message = "databaseType must not be blank")
        String databaseType,
        @NotBlank(message = "host must not be blank")
        String host,
        @NotNull(message = "port must not be null")
        int port,
        String customUrl, // customUrl can be null
        @NotBlank(message = "databaseName must not be blank")
        String databaseName,
        @NotBlank(message = "username must not be blank")
        String username,
        @NotBlank(message = "password must not be blank")
        String password
) {
}
