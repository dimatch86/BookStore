package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findTagEntityBySlug(String slug);
}
