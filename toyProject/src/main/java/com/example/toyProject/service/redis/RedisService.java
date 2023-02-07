package com.example.toyProject.service.redis;

import com.example.toyProject.dto.enums.ErrorCode;
import com.example.toyProject.entity.redis.refreshToken.AccessToken;
import com.example.toyProject.entity.redis.CertToken;
import com.example.toyProject.entity.redis.refreshToken.RefreshToken;
import com.example.toyProject.exception.jwt.ExpiredRefreshTokenException;
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
    public void certTokenSave(String email, Long cert) {
        CertToken certToken = new CertToken(email, cert);
        certTokenRepository.save(certToken);
    }

    @Transactional
    public void refreshTokenSave(String uuid, AccessToken accessToken) {
        RefreshToken refreshToken = new RefreshToken(uuid, accessToken);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken refreshTokenFindById(String uuid) {
        return refreshTokenRepository.findById(uuid)
                .orElseThrow(
                        () -> new ExpiredRefreshTokenException("Refresh Token 기한이 만료되었습니다.", ErrorCode.EXPIRED_REFRESH_TOKEN)
                );
    }

    public CertToken certTokenFindByEmail(String email) {
        return certTokenRepository.findById(email).orElse(null);
    }

    @Transactional
    public void refreshTokenDeleteById(String uuid) {
        refreshTokenRepository.deleteById(uuid);
    }
}
