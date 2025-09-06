package com.nhalamphitrentroi.testapp1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhalamphitrentroi.testapp1.entity.Database;

@Repository
public interface DatabaseRepository extends JpaRepository<Database, String> {
    
    List<Database> findAllByOrderByDatabaseName();
}
