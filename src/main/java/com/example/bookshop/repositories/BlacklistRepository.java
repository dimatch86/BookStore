package com.example.bookshop.repositories;


import com.example.bookshop.model.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistToken, Long> {

    boolean existsByToken(String token);
    void deleteByExpiredLessThan(Date date);
}
