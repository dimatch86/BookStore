package com.example.bookshop.services;

import com.example.bookshop.model.entity.author.Author;
import com.example.bookshop.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private List<Author> getAuthorsList() {
        return authorRepository.findAll();
    }

    public SortedMap<Character, Set<Author>> getAuthorsData() {
        TreeMap<Character, Set<Author>> authorsData = new TreeMap<>();
        List<Author> authors = getAuthorsList();
        authors.forEach(author -> {
            char mapKey = author.getName().toUpperCase().charAt(0);
            if (authorsData.containsKey(mapKey)) {
                authorsData.get(mapKey).add(author);
            } else {
                Set<Author> authorSet = new TreeSet<>();
                authorSet.add(author);
                authorsData.put(mapKey, authorSet);
            }
        });
        return new TreeMap<>(authorsData);
    }

    public Author getAuthorBySlug(String slug) {

        return authorRepository.findAuthorEntityBySlug(slug);
    }
}
