package com.example.toyProject.service.email;

import com.example.toyProject.dto.EmailMessageDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Override
    public Long sendCertMail(EmailMessageDto emailMessageDto, String html) {
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

        return cert;
    }

    private String setContext(String code, String html) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(html, context);
    }

    private Long createCert() {
        Random random = new Random();
        return Math.abs(random.nextLong()%100_000_0);
    }
}
