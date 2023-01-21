package com.example.toyProject.dto.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(value = "refreshToken", timeToLive = (3600*24*14))
public class RefreshTokenDto {

    private String id;
    private String accessToken;

    public RefreshTokenDto(String id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
