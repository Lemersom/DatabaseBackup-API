package com.example.backupvault.controller;

import com.example.backupvault.dto.DatabaseConfigDTO;
import com.example.backupvault.dto.DatabaseConfigResponseDTO;
import com.example.backupvault.model.DatabaseConfigModel;
import com.example.backupvault.service.DatabaseConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/database-config")
public class DatabaseConfigController {

    private final DatabaseConfigService databaseConfigService;

    public DatabaseConfigController(DatabaseConfigService databaseConfigService) {
        this.databaseConfigService = databaseConfigService;
    }

    @GetMapping
    public ResponseEntity<List<DatabaseConfigResponseDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "size", defaultValue = "20") Integer size) {
        List<DatabaseConfigResponseDTO> databaseConfigListPage = databaseConfigService.findAll(page, size);

        return ResponseEntity.ok(databaseConfigListPage);
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<Object> findById(@PathVariable Long requestedId) {
        Optional<DatabaseConfigResponseDTO> databaseConfig = databaseConfigService.findById(requestedId);
        if(databaseConfig.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(databaseConfig.get());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody DatabaseConfigDTO databaseConfigDTO, UriComponentsBuilder ucb) {
        DatabaseConfigModel savedDatabaseConfig = databaseConfigService.save(databaseConfigDTO);

        URI locationOfNewDatabaseConfig = ucb
                .path("database-config/{id}")
                .buildAndExpand(savedDatabaseConfig.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewDatabaseConfig).build();
    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<Void> delete(@PathVariable Long requestedId) {
        if(databaseConfigService.delete(requestedId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
