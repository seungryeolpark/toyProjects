package com.example.toyProject.service.redis;

import com.example.toyProject.annotation.NoLogging;
import com.example.toyProject.dto.CertTokenDto;
import com.example.toyProject.repository.CertTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class RedisService {

    private final CertTokenRepository certTokenRepository;

    @Transactional
    @NoLogging
    public void certTokenRedisSave(String email, Long cert) {
        CertTokenDto certTokenDto = new CertTokenDto(email, cert);
        certTokenRepository.save(certTokenDto);
    }
}
