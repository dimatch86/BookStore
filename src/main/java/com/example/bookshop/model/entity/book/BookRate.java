package com.example.bookshop.model.entity.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Entity
@Table(name = "`book_rate`")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private long bookId;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private long userId;

    @Column(name = "value", columnDefinition = "INT NOT NULL")
    @Max(value = 5, message = "Rate should not be greater than 5")
    private int value;

    @Column(name = "time", columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Book book;
}
