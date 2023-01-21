package com.example.toyProject.repository;

import com.example.toyProject.entity.db.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
