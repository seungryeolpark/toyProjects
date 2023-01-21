package com.example.toyProject.service.cookie;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    public void createCookie(String uuid, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshTokenId", uuid);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
