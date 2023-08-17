package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.links.Book2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Long> {
    Book2User findBook2UserByBookId(long id);
    Book2User findBook2UserByBookIdAndUserId(long bookId, long userId);

    void deleteBook2UserByBookIdAndUserId(long bookId, long userId);
}
