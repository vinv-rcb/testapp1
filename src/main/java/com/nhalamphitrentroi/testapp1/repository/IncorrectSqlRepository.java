package com.nhalamphitrentroi.testapp1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nhalamphitrentroi.testapp1.entity.IncorrectSql;

@Repository
public interface IncorrectSqlRepository extends JpaRepository<IncorrectSql, Long> {
    
    /**
     * Find incorrect SQL entries by database name
     */
    List<IncorrectSql> findByDatabaseNameOrderByCreatedAtDesc(String databaseName);
    
    /**
     * Find all incorrect SQL entries ordered by creation date
     */
    List<IncorrectSql> findAllByOrderByCreatedAtDesc();
    
    /**
     * Find incorrect SQL entries by error reason
     */
    @Query("SELECT i FROM IncorrectSql i WHERE i.errorReason LIKE %:errorReason% ORDER BY i.createdAt DESC")
    List<IncorrectSql> findByErrorReasonContaining(@Param("errorReason") String errorReason);
    
    /**
     * Count incorrect SQL entries by database name
     */
    long countByDatabaseName(String databaseName);
    
    /**
     * Count all incorrect SQL entries
     */
    long countAllBy();
}
