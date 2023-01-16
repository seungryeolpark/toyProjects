package com.example.toyProject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {

    private String token;

    @Builder
    public TokenDto(String token) {
        this.token = token;
    }
}
