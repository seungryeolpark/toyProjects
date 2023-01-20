package com.example.toyProject.config;

import com.example.toyProject.filter.ExceptionHandlerFilter;
import com.example.toyProject.jwt.TokenProvider;
import com.example.toyProject.jwt.error.JwtAccessDeniedHandler;
import com.example.toyProject.jwt.error.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize 어노테이션을 메소드단위로 추가하기 위해 적용
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public static final String[] PERMIT_ALL_PATTERNS = {"/api/authenticate",
            "/api/signup", "/api/send-cert/{email}"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token 을 사용하는 방식이기 때문에 csrf 를 disable 합니다.
                .csrf().disable()

                // exception handling 부분 ex) 401, 403 에러
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 세션을 사용하지 않기 때문에 세션 설정을 STATELESS 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_ALL_PATTERNS).permitAll()
                .anyRequest().authenticated()

                .and()
                .cors()

                // UsernamePasswordAuthenticationFilter 는 json 형식을 받지 못하기 때문에
                // 커스텀 필터를 사용해야 하는데 request body 에 json 형식으로 주고받는 것보단
                // Controller 에서 로직을 짜는게 간편하고 별차이 없어보이기에
                // formLogin() 기능을 비활성화하였다.
//                .and()
//                .formLogin()

                // 필터 적용
                .and()
                .addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
                .apply(new JwtSecurityConfig(tokenProvider));

        return httpSecurity.build();
    }

    // Cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
