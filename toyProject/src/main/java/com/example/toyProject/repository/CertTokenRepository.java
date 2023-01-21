package com.example.toyProject.repository;

import com.example.toyProject.entity.redis.CertToken;
import org.springframework.data.repository.CrudRepository;

public interface CertTokenRepository extends CrudRepository<CertToken, String> {
}
