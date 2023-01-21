package com.example.toyProject.entity.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(value = "refreshToken", timeToLive = (3600*24*14))
public class RefreshToken {

    private String id;
    private AccessToken accessToken;

    public RefreshToken(String id, AccessToken accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
