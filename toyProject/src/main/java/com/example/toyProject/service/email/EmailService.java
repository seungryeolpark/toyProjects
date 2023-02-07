package com.example.toyProject.service.email;

import com.example.toyProject.annotation.DuplicationEmailCheck;
import com.example.toyProject.dto.EmailMessageDto;
import com.example.toyProject.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RedisService redisService;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public static final String EMAIL_CERT_HTML = "cert_email";
    public static final String EMAIL_CERT_SUBJECT = "[toyProject] 이메일 인증번호";

    @DuplicationEmailCheck
    public void sendCertMail(EmailMessageDto emailMessageDto, String html) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        Long cert = createCert();

        emailMessageDto.setCode(cert.toString());

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessageDto.getTo());
            mimeMessageHelper.setSubject(emailMessageDto.getSubject());
            mimeMessageHelper.setText(setContext(emailMessageDto.getCode(), html), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        redisService.certTokenSave(emailMessageDto.getTo(), cert);
    }

    private String setContext(String code, String html) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(html, context);
    }

    private Long createCert() {
        Random random = new Random();
        return random.nextLong(100_000, 100_000_0);
    }
}
