package com.example.databasebackup.repository;

import com.example.databasebackup.model.DatabaseConfigModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseConfigRepository extends JpaRepository<DatabaseConfigModel, Long> {
}
