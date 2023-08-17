package com.example.bookshop.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Long> {

    boolean existsByEmail(String email);

    BookstoreUser findBookstoreUserByEmail(String email);
}
