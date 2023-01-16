package com.example.toyProject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailMessageDto {

    private String to;
    private String subject;
    private String code;

    @Builder
    public EmailMessageDto(String to, String subject, String code) {
        this.to = to;
        this.subject = subject;
        this.code = code;
    }
}
