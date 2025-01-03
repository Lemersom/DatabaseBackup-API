package com.example.databasebackup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatabaseConfigDTO(
        @NotBlank(message = "databaseType must not be blank")
        String databaseType,
        @NotBlank(message = "host must not be blank")
        String host,
        @NotNull(message = "port must not be null")
        int port,
        @NotBlank(message = "databaseName must not be blank")
        String databaseName,
        String customUrl, // customUrl can be null
        @NotBlank(message = "username must not be blank")
        String username,
        @NotBlank(message = "password must not be blank")
        String password
) {
}
