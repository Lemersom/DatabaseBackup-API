package com.example.backupdatabase.dto;

public record DatabaseConfigResponseDTO(
        String databaseType,
        String host,
        int port,
        String databaseName,
        String customUrl
) {
}
