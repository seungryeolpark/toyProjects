package com.example.toyProject.filter;

import com.example.toyProject.config.SecurityConfig;
import com.example.toyProject.dto.enums.ErrorCode;
import com.example.toyProject.exception.jwt.EmptyTokenException;
import com.example.toyProject.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    // 토큰의 인증정보를 SecurityContext 에 저장
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        for (String pattern : SecurityConfig.PERMIT_ALL_PATTERNS) {
            if (requestURI.equals(pattern)) {
                chain.doFilter(request, response);
                return;
            }
        }

        if (StringUtils.hasText(jwt)) { // jwt 토큰이 있는 경우
            if (tokenProvider.validateToken(jwt)) { // jwt 토큰 유효성 검사 유효할 경우 true

                // 인증 정보가 존재하지 않는 경우 인증 정보 갱신
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Security Context 에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                }

            } else {
                log.info("[info] 유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            }
        } else { // jwt 토큰이 없는 경우
            throw new EmptyTokenException("JWT 토큰이 없습니다", ErrorCode.EMPTY_ACCESS_TOKEN);
        }

        chain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보를 꺼내옴
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
