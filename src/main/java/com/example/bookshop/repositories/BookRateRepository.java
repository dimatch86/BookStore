package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.BookRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRateRepository extends JpaRepository<BookRate, Long> {
    BookRate findBookRatesByBookIdAndUserId(Long bookId, Long userId);
}
