package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.review.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<BookReview, Long> {
}
