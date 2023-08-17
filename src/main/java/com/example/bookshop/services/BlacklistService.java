package com.example.bookshop.services;

import com.example.bookshop.errs.BlacklistException;
import com.example.bookshop.model.entity.BlacklistToken;
import com.example.bookshop.repositories.BlacklistRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    @Value("${auth.secret}")
    private String secret;

    @Transactional
    @SuppressWarnings("java:S1874")
    public void pushTokenToBlackList(String token) {

        if (token != null) {
            Date expiration = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                    .getBody().getExpiration();
            BlacklistToken blacklistToken = BlacklistToken.builder()
                    .token(token)
                    .expired(expiration)
                    .build();

            blacklistRepository.save(blacklistToken);
        }
    }

    public void validateToken(String token) throws BlacklistException {
        if (blacklistRepository.existsByToken(token)) {
            log.warn("Зафиксирована попытка входа с невалидным токеном");
            throw new BlacklistException("Token is in Blacklist");
        }
    }

    @Scheduled(cron = "${scheduled-tasks.blacklist}")
    @Transactional
    public void deleteExpiredTokens() {
        blacklistRepository.deleteByExpiredLessThan(new Date());
    }
}