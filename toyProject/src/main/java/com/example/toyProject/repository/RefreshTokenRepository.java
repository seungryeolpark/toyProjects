package com.example.toyProject.repository;

import com.example.toyProject.entity.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
