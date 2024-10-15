package com.example.backupvault.repository;

import com.example.backupvault.model.DatabaseConfigModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseConfigRepository extends JpaRepository<DatabaseConfigModel, Long> {
}
