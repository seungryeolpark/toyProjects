package com.example.toyProject.controller;

import com.example.toyProject.dto.LoginDto;
import com.example.toyProject.dto.TokenDto;
import com.example.toyProject.filter.JwtFilter;
import com.example.toyProject.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // SecurityConfig 에 FormLogin() 을 비활성화 했기에
    // UsernamePasswordAuthenticationFilter 를 사용할 수 없어
    // 직접 UsernamePasswordAuthenticationToken 을 만들어
    // authenticate 메서드를 통해 Authentication 객체를 얻어와 인증 정보 갱신
    // 인증 정보를 토대로 Jwt 토큰을 만들어 Response headers 에 Jwt 토큰을 넣어둠
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);

        return new ResponseEntity<>(TokenDto.builder()
                .token(jwt).build(), httpHeaders, HttpStatus.OK);
    }
}
