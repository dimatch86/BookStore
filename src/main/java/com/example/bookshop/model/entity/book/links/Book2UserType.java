package com.example.bookshop.model.entity.book.links;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book2user_type")
@Data
public class Book2UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;
}
