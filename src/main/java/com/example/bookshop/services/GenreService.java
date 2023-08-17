package com.example.bookshop.services;

import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.genre.Genre;
import com.example.bookshop.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public List<Book> getBooksForParents(Genre genreEntity) {

        List<Book> booksList = new ArrayList<>();
        List<Genre> genreEntities = genreRepository.getGenreEntitiesById(genreEntity.getId());

        for (Genre entity : genreEntities) {
            booksList.addAll(entity.getBooks());
        }
        return booksList;
    }

    public List<Genre> parentGenres(){
        return genreRepository.getParentGenreEntities();
    }

    public Genre getGenre(String slug) {
        Genre genreEntity = genreRepository.findGenreEntityBySlug(slug);
        if (genreEntity.getBooks().isEmpty()) {
            genreEntity.setBooks(getBooksForParents(genreEntity));
        }

        return genreEntity;
    }
}
