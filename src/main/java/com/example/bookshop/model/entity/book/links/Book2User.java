package com.example.bookshop.model.entity.book.links;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(name = "type_id", columnDefinition = "INT NOT NULL")
    private long typeId;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private long bookId;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private long userId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Book2UserType book2UserType;
}
