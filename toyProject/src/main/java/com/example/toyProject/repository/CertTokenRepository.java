package com.example.toyProject.repository;

import com.example.toyProject.dto.CertTokenDto;
import org.springframework.data.repository.CrudRepository;

public interface CertTokenRepository extends CrudRepository<CertTokenDto, String> {
}
