package com.example.bookshop.security;

import com.example.bookshop.errs.BlacklistException;
import com.example.bookshop.model.ApiResponse;
import com.example.bookshop.security.jwt.JWTUtil;
import com.example.bookshop.services.BlacklistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BlacklistTokenFilter extends OncePerRequestFilter {

    private final BlacklistService blacklistService;
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtUtil.getJwt(request);
            if (token != null) {
                blacklistService.validateToken(token);
            }
            filterChain.doFilter(request, response);

        } catch (BlacklistException e) {
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getLocalizedMessage())
                    .timeStamp(LocalDateTime.now())
                    .build();

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(convertObjectToJson(apiResponse));
        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(object);
    }
}