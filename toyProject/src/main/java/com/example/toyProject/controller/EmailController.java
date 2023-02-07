package com.example.toyProject.controller;

import com.example.toyProject.dto.EmailMessageDto;
import com.example.toyProject.service.email.EmailService;
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

    private final EmailService emailService;

    @GetMapping("/send-cert/email/{email}")
    public void sendCertEmail(@PathVariable("email") @Email String email) {
        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .to(email)
                .subject(EmailService.EMAIL_CERT_SUBJECT).build();

        emailService.sendCertMail(emailMessageDto, EmailService.EMAIL_CERT_HTML);
    }
}
