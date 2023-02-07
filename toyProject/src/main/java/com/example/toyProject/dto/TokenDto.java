package com.example.toyProject.dto;

import com.example.toyProject.entity.redis.refreshToken.AccessToken;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {

    private AccessToken accessToken;
    private String refreshToken;

    @Builder
    public TokenDto(AccessToken accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
