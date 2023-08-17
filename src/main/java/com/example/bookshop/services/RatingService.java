package com.example.bookshop.services;

import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.book.BookRate;
import com.example.bookshop.repositories.BookRateRepository;
import com.example.bookshop.repositories.BookRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    @Value("${book.rating.capacity}")
    private int bookRatingCapacity;
    private final BookRepository bookRepository;
    private final BookRateRepository bookRateRepository;


    public int getMyRate(String slug) {

        if (CurrentUserUtils.checkUserAuthenticated()) {
            Book book = bookRepository.findBookEntityBySlug(slug);
            BookstoreUser user = CurrentUserUtils.getCurrentUser();
            BookRate bookRate =
                    Optional.ofNullable(bookRateRepository.findBookRatesByBookIdAndUserId(book.getId(), user.getId()))
                            .orElse(new BookRate());
            return bookRate.getValue();
        }
        return 0;
    }

    private int getAverageBookRate(List<BookRate> bookRates) {

        return (int) Math.round(bookRates.stream()
                .map(BookRate::getValue).mapToInt(value -> value).average().orElse(0));
    }

    public boolean[] mapAverageBookRatingToStarsArray(List<BookRate> bookRates) {

        int averRating = getAverageBookRate(bookRates);

        return mapRateToStarsArray(averRating);
    }

    public boolean[] mapRateToStarsArray(int rate) {

        boolean[] stars = new boolean[bookRatingCapacity];

        for (int i = 0; i < rate; i++) {
            stars[i] = true;
        }

        return stars;
    }

    public Map<Integer, Long> getDistributionRatesMap(List<BookRate> rateList) {
        return rateList.stream()
                .collect(Collectors.groupingBy(BookRate::getValue, Collectors.counting()));
    }

    @Transactional
    public void rateBook(String slug, int value) {

        Book book = bookRepository.findBookEntityBySlug(slug);
        BookstoreUser user = CurrentUserUtils.getCurrentUser();

        BookRate bookRate = bookRateRepository.findBookRatesByBookIdAndUserId(book.getId(), user.getId());
        if (bookRate == null) {
            bookRate = BookRate.builder()
                    .bookId(book.getId())
                    .userId(user.getId())
                    .value(value)
                    .time(LocalDateTime.now())
                    .build();
            bookRateRepository.save(bookRate);

        } else {
            bookRate.setValue(value);
            bookRate.setTime(LocalDateTime.now());
            bookRateRepository.save(bookRate);
        }
    }
}