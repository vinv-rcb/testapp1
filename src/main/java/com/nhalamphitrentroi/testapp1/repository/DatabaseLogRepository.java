package com.nhalamphitrentroi.testapp1.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    /**
     * Find unexpected queries with exe_time > 500 AND exe_count > 100
     * with optional search by sql and database_name
     */
    @Query("SELECT d FROM DatabaseLog d WHERE " +
           "d.exeTime > 500 AND d.exeCount > 100 AND " +
           "(:sql IS NULL OR LOWER(d.sql) LIKE LOWER(CONCAT('%', :sql, '%'))) AND " +
           "(:databaseName IS NULL OR LOWER(d.databaseName) LIKE LOWER(CONCAT('%', :databaseName, '%'))) " +
           "ORDER BY d.exeTime DESC, d.exeCount DESC")
    Page<DatabaseLog> findUnexpectedQueries(@Param("sql") String sql,
                                            @Param("databaseName") String databaseName,
                                            Pageable pageable);
    
    /**
     * Count unexpected queries with exe_time > 500 AND exe_count > 100
     * with optional search by sql and database_name
     */
    @Query("SELECT COUNT(d) FROM DatabaseLog d WHERE " +
           "d.exeTime > 500 AND d.exeCount > 100 AND " +
           "(:sql IS NULL OR LOWER(d.sql) LIKE LOWER(CONCAT('%', :sql, '%'))) AND " +
           "(:databaseName IS NULL OR LOWER(d.databaseName) LIKE LOWER(CONCAT('%', :databaseName, '%')))")
    long countUnexpectedQueries(@Param("sql") String sql, @Param("databaseName") String databaseName);
    
    /**
     * Find problematic logs with exe_time > 500 OR exe_count > 100
     * Used by suggestion job
     */
    @Query("SELECT d FROM DatabaseLog d WHERE " +
           "d.exeTime > 500 OR d.exeCount > 100 " +
           "ORDER BY d.exeTime DESC, d.exeCount DESC")
    List<DatabaseLog> findProblematicLogs();
}
