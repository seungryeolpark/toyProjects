package com.example.toyProject.controller;

import com.example.toyProject.dto.EmailMessageDto;
import com.example.toyProject.service.email.EmailService;
import com.example.toyProject.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final RedisService redisService;
    private final EmailService emailService;

    private final String EMAIL_CERT_HTML = "cert_email";
    private final String EMAIL_CERT_SUBJECT = "[toyProject] 이메일 인증번호";

    @GetMapping("/send-cert/{email}")
    public void sendCertEmail(@PathVariable("email") @Email String email) {
        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .to(email)
                .subject(EMAIL_CERT_SUBJECT).build();

        Long cert = emailService.sendCertMail(emailMessageDto, EMAIL_CERT_HTML);
        redisService.certTokenRedisSave(email, cert);
    }
}
