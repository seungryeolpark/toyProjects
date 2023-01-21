package com.example.toyProject.service.redis;

import com.example.toyProject.entity.redis.AccessToken;
import com.example.toyProject.entity.redis.CertToken;
import com.example.toyProject.entity.redis.RefreshToken;
import com.example.toyProject.repository.CertTokenRepository;
import com.example.toyProject.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisService {

    private final CertTokenRepository certTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void certTokenRedisSave(String email, Long cert) {
        CertToken certToken = new CertToken(email, cert);
        certTokenRepository.save(certToken);
    }

    @Transactional
    public void refreshTokenRedisSave(String uuid, AccessToken accessToken) {
        RefreshToken refreshToken = new RefreshToken(uuid, accessToken);
        refreshTokenRepository.save(refreshToken);
    }
}
