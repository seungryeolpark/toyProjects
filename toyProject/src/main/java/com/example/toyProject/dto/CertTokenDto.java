package com.example.toyProject.dto;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "certToken", timeToLive = 86400) // test 용으로 만료시간 1일
public class CertTokenDto {

    @Id
    private String certId;
    private Long certValue;

    public CertTokenDto(final String certId, final Long certValue) {
        this.certId = certId;
        this.certValue = certValue;
    }
}
