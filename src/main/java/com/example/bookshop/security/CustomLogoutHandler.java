package com.example.bookshop.security;

import com.example.bookshop.security.jwt.JWTUtil;
import com.example.bookshop.services.BlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final BlacklistService blacklistService;
    private final JWTUtil jwtUtil;
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        String token = jwtUtil.getJwt(httpServletRequest);
        blacklistService.pushTokenToBlackList(token);
        log.info("LOGOUT!!!!!");
    }
}