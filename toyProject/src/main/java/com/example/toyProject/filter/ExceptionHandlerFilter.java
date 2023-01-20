package com.example.toyProject.filter;

import com.example.toyProject.dto.ErrorResponseDto;
import com.example.toyProject.dto.enums.ErrorCode;
import com.example.toyProject.exception.jwt.EmptyTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// JwtFilter 에서 Exception 이 발생할 경우
// Exception ErrorCode 를 Response 에 json 형식으로 넣어 처리
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (EmptyTokenException e) {
            sendErrorResponse(response, ErrorCode.EMPTY_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, ErrorCode.Malformed_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, ErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            sendErrorResponse(response, ErrorCode.UN_SUPPORT_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            sendErrorResponse(response, ErrorCode.IllegalArgument);
        } catch (Exception e) {
            sendErrorResponse(response, ErrorCode.EXCEPTION);
        }

    }

    private static void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        log.error("[error] {} {} {}", errorCode.getStatus(), errorCode.getErrorCode(), errorCode.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
