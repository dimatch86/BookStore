package com.example.bookshop.model.entity.book.review;


import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "book_review")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReview implements Comparable<BookReview>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private long bookId;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private long userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time = LocalDateTime.now();

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "bookReview")
    private List<BookReviewLike> reviewLikes = new ArrayList<>();

    public List<BookReviewLike> likes() {
        return reviewLikes.stream().filter(like -> like.getValue() == 1).collect(Collectors.toList());
    }

    public List<BookReviewLike> disLikes() {
        return reviewLikes.stream().filter(like -> like.getValue() == -1).collect(Collectors.toList());
    }

    public boolean[] reviewRatingStars() {

        boolean[] stars = new boolean[5];

        if (!reviewLikes.isEmpty()) {

            float percentOfLikes = (float) likes().size() / reviewLikes.size();
            int rate = Math.round(5 * percentOfLikes);

            for (int i = 0; i < rate; i++) {
                stars[i] = true;
            }
        }
        return stars;
    }

    @Override
    public int compareTo(BookReview bookReview) {
        if (this.getTime().isBefore(bookReview.getTime())) {
            return 1;
        } else if (this.getTime().equals(bookReview.getTime())) {
            return 0;
        }
        return -1;
    }
}
