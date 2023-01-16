package com.example.toyProject.repository;

import com.example.toyProject.repository.CertTokenRepository;
import com.example.toyProject.dto.CertTokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CertTokenRepositoryTest {

    @Autowired
    CertTokenRepository certTokenRepository;

    @DisplayName("Redis 실행")
    @Test
    public void RedisSaveLoadTest() {
        // given
        Random random = new Random();
        String key = "test";
        Long value = Math.abs(random.nextLong()%100_000_0);
        CertTokenDto certTokenDto = new CertTokenDto(key, value);

        // when
        certTokenRepository.save(certTokenDto);
        Optional<CertTokenDto> temp = certTokenRepository.findById(key);

        // then
        CertTokenDto result = temp.get();
        assertThat(result.getCertValue()).isEqualTo(value);
    }

}