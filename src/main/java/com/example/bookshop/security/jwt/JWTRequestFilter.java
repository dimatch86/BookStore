package com.example.bookshop.security.jwt;

import com.example.bookshop.security.BookstoreUserDetails;
import com.example.bookshop.security.BookstoreUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = jwtUtil.getJwt(httpServletRequest);
            String username = null;
            if (token != null) {
                username = jwtUtil.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                BookstoreUserDetails userDetails =
                        (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(jwtUtil.validateToken(token, userDetails))) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ExpiredJwtException e) {
            Cookie[] cookies = httpServletRequest.getCookies();
            for (Cookie cookie: cookies) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                httpServletResponse.addCookie(cookie);
            }
            log.warn(e.getLocalizedMessage());
        }
    }
}
