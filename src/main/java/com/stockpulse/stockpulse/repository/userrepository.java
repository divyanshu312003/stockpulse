package com.stockpulse.stockpulse.repository;
import com.stockpulse.stockpulse.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
    public interface userrepository extends JpaRepository<user, Long> {
        Optional<user> findByEmail(String email);
    }

