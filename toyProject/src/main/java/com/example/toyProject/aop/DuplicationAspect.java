package com.example.toyProject.aop;

import com.example.toyProject.dto.EmailMessageDto;
import com.example.toyProject.dto.MemberDto;
import com.example.toyProject.dto.enums.ErrorCode;
import com.example.toyProject.exception.duplication.DuplicateEmailException;
import com.example.toyProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class DuplicationAspect {

    private final MemberRepository memberRepository;

    @Around("@annotation(com.example.toyProject.annotation.DuplicationEmailCheck)")
    public Object DuplicationEmailCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.getArgs()[0];

        if (object instanceof EmailMessageDto) {
            duplicationEmail( ((EmailMessageDto) object).getTo());
        } else if (object instanceof MemberDto) {
            duplicationEmail( ((MemberDto) object).getEmail());
        }

        Object result = joinPoint.proceed();
        return result;
    }

    private void duplicationEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(s -> {
            throw new DuplicateEmailException(
                    "이미 가입되어 있는 이메일입니다.",
                    ErrorCode.DUPLICATION_EMAIL);
        });
    }
}
