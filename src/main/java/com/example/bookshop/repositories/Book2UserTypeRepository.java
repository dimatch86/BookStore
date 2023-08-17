package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.links.Book2UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserTypeRepository extends JpaRepository<Book2UserType, Integer> {
    Book2UserType findByCode(String book2UserTypeEnum);
}
