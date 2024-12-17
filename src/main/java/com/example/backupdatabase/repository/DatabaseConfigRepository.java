package com.example.backupdatabase.repository;

import com.example.backupdatabase.model.DatabaseConfigModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseConfigRepository extends JpaRepository<DatabaseConfigModel, Long> {
}
