package com.example.backupvault.dto;

public record DatabaseConfigResponseDTO(
        String databaseType,
        String host,
        int port,
        String databaseName
) {
}
