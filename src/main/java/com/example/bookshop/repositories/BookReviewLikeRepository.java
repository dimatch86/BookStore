package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.review.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Long> {

    BookReviewLike findBookReviewLikeByBookReviewIdAndUserId(long reviewId, long userId);
}