package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.file.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    BookFile findBookFileEntityByHash(String hash);
}
