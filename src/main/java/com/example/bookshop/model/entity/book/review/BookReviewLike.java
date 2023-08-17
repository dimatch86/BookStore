package com.example.bookshop.model.entity.book.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "review_id", columnDefinition = "INT NOT NULL")
    private long reviewId;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private long userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time = LocalDateTime.now();

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BookReview bookReview;
}
