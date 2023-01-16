package com.example.toyProject.service.email;

import com.example.toyProject.dto.EmailMessageDto;

public interface EmailService {
    public Long sendCertMail(EmailMessageDto emailMessageDto, String html);

}
