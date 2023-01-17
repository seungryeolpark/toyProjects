package com.example.toyProject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("execution(* com.example.toyProject.*.*(..)) &&" +
            "!@target(com.example.toyProject.annotation.NoLogging)")
    public void TimeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getName();
        String method = joinPoint.getSignature().getName();

        StopWatch stopWatch = new StopWatch();
        log.info("[log] {}.{} Start", className, method);

        stopWatch.start();
        joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("[log] {}.{} End, {}ms", className, method, totalTimeMillis);
    }
}
