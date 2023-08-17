package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findAuthorEntityBySlug(String slug);
}
