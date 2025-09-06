package com.nhalamphitrentroi.testapp1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nhalamphitrentroi.testapp1.entity.DatabaseLog;

@Repository
public interface DatabaseLogRepository extends JpaRepository<DatabaseLog, Long> {
    
    List<DatabaseLog> findByDatabaseNameOrderByCreatedAtDesc(String databaseName);
    
    @Query("SELECT d FROM DatabaseLog d WHERE d.databaseName = :databaseName ORDER BY d.createdAt DESC")
    List<DatabaseLog> findLogsByDatabaseName(@Param("databaseName") String databaseName);
    
    @Query("SELECT d FROM DatabaseLog d ORDER BY d.createdAt DESC")
    List<DatabaseLog> findAllLogsOrderByCreatedAtDesc();
}
