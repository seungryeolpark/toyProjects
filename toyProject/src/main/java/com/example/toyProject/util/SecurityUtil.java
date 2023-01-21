package com.example.toyProject.util;

import com.example.toyProject.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SecurityUtil {

    private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);
    public static Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context 에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = String.valueOf(springSecurityUser);
        }

        return Optional.ofNullable(username);
    }

    // Request Header 에서 토큰 정보를 꺼내옴
    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String convertUriPattern(String uri) {
        String[] patterns = uri.split("/");
        String result = "";

        for (String pattern : patterns) {
            if (pattern.equals("")) continue;
            else if (isEmail(pattern)) pattern = "{email}";

            result += "/"+pattern;
        }
        return result;
    }

    private static boolean isEmail(String pattern) {
        Matcher m = EMAIL_PATTERN.matcher(pattern);
        if (m.matches()) return true;
        return false;
    }
}
