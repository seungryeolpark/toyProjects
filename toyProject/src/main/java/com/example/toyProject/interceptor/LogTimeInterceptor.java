package com.example.toyProject.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogTimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        long currentTime = System.currentTimeMillis();

        request.setAttribute("start", currentTime);
        log.info("[log] {} start", request.getRequestURI());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        long currentTime = System.currentTimeMillis();
        long startTime = (long) request.getAttribute("start");
        long totalTime = currentTime-startTime;

        log.info("[log] {} end, {}ms", request.getRequestURI(), totalTime);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
