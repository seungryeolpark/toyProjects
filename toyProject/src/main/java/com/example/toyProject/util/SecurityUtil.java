package com.example.toyProject.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUtil {

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
}
