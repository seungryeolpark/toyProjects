package com.example.toyProject.entity.redis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {

    private String ip;
    private String username;
    private String jwt;

    @Builder
    public AccessToken(String ip, String username, String jwt) {
        this.ip = ip;
        this.username = username;
        this.jwt = jwt;
    }
}
