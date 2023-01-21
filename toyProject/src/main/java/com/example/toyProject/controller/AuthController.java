package com.example.toyProject.controller;

import com.example.toyProject.dto.LoginDto;
import com.example.toyProject.dto.TokenDto;
import com.example.toyProject.dto.enums.ErrorCode;
import com.example.toyProject.exception.jwt.EmptyRefreshTokenException;
import com.example.toyProject.filter.JwtFilter;
import com.example.toyProject.service.member.MemberService;
import com.example.toyProject.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // SecurityConfig 에 FormLogin() 을 비활성화 했기에
    // UsernamePasswordAuthenticationFilter 를 사용할 수 없어
    // 직접 UsernamePasswordAuthenticationToken 을 만들어
    // authenticate 메서드를 통해 Authentication 객체를 얻어와 인증 정보 갱신
    // 인증 정보를 토대로 Jwt 토큰을 만들어 Response headers 에 Jwt 토큰을 넣어둠
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletRequest request) {

        return memberService.login(loginDto, request);
    }

    @GetMapping("/check/refreshToken")
    public ResponseEntity<TokenDto> checkRefreshToken(
            @RequestHeader(value = JwtFilter.REFRESH_TOKEN_HEADER, defaultValue = "") String refreshTokenId,
            HttpServletRequest request) {
        return memberService.checkRefreshToken(refreshTokenId, request);
    }
}
