package com.example.bookshop.model.entity.book.links;


import javax.persistence.*;

@Entity
@Table(name = "book2genre")
public class Book2Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private int bookId;

    @Column(name = "genre_id", columnDefinition = "INT NOT NULL")
    private int genreId;
}
