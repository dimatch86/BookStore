package com.example.bookshop.services;

import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.book.review.BookReview;
import com.example.bookshop.model.entity.book.review.BookReviewLike;
import com.example.bookshop.model.entity.user.User;
import com.example.bookshop.repositories.BookRepository;
import com.example.bookshop.repositories.BookReviewLikeRepository;
import com.example.bookshop.repositories.ReviewRepository;
import com.example.bookshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;


    @Transactional
    public void saveReview(String slug, String text) {

        Book book = Optional.ofNullable(bookRepository.findBookEntityBySlug(slug)).orElse(new Book());
        User testUser = getTestUser();

        BookReview bookReview = BookReview.builder()
                .bookId(book.getId())
                .userId(testUser.getId())
                .time(LocalDateTime.now())
                .text(text)
                .build();

        reviewRepository.save(bookReview);
    }

    @Transactional
    public void saveReviewLike(long reviewId, short value) {

        User testUser = getTestUser();
        BookReviewLike reviewLike = bookReviewLikeRepository
                .findBookReviewLikeByBookReviewIdAndUserId(reviewId, testUser.getId());

        if (reviewLike == null) {
            BookReviewLike bookReviewLike = BookReviewLike.builder()
                    .reviewId(reviewId)
                    .userId(testUser.getId())
                    .time(LocalDateTime.now())
                    .value(value)
                    .build();

            bookReviewLikeRepository.save(bookReviewLike);
        }
    }

    public List<BookReview> getSortedReviewList(Book book) {

        List<BookReview> bookReviews = book.getBookReviews();
        Collections.sort(bookReviews);
        return bookReviews;
    }

    private User getTestUser() {
        return Optional.ofNullable(userRepository.findUserByName("Test User")).orElse(new User());
    }
}