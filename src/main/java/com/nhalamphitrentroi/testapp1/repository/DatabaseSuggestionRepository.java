package com.nhalamphitrentroi.testapp1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nhalamphitrentroi.testapp1.entity.DatabaseSuggestion;

@Repository
public interface DatabaseSuggestionRepository extends JpaRepository<DatabaseSuggestion, String> {
    
    /**
     * Find suggestions by database name
     */
    List<DatabaseSuggestion> findByDatabaseNameOrderByCreatedAtDesc(String databaseName);
    
    /**
     * Find suggestions by resolution status
     */
    List<DatabaseSuggestion> findByIsResolvedOrderByCreatedAtDesc(Boolean isResolved);
    
    /**
     * Find suggestions by database name and resolution status
     */
    List<DatabaseSuggestion> findByDatabaseNameAndIsResolvedOrderByCreatedAtDesc(String databaseName, Boolean isResolved);
    
    /**
     * Count suggestions by resolution status
     */
    long countByIsResolved(Boolean isResolved);
    
    /**
     * Count suggestions by database name
     */
    long countByDatabaseName(String databaseName);
    
    /**
     * Find suggestions containing specific SQL
     */
    @Query("SELECT d FROM DatabaseSuggestion d WHERE d.sql LIKE %:sql% ORDER BY d.createdAt DESC")
    List<DatabaseSuggestion> findBySqlContaining(@Param("sql") String sql);
    
    /**
     * Find suggestions by exact SQL match
     */
    @Query("SELECT d FROM DatabaseSuggestion d WHERE d.sql = :sql ORDER BY d.createdAt DESC")
    List<DatabaseSuggestion> findByExactSql(@Param("sql") String sql);
    
    /**
     * Check if suggestion exists for exact SQL
     */
    @Query("SELECT COUNT(d) > 0 FROM DatabaseSuggestion d WHERE d.sql = :sql")
    boolean existsByExactSql(@Param("sql") String sql);
    
    /**
     * Check if suggestion exists by SQL hash
     */
    @Query("SELECT COUNT(d) > 0 FROM DatabaseSuggestion d WHERE d.sqlHash = :sqlHash")
    boolean existsBySqlHash(@Param("sqlHash") String sqlHash);
    
    /**
     * Find all suggestions ordered by creation date
     */
    @Query("SELECT d FROM DatabaseSuggestion d ORDER BY d.createdAt DESC")
    List<DatabaseSuggestion> findAllOrderByCreatedAtDesc();
}
