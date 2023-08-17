package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.BooksPageDto;
import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.genre.Genre;
import com.example.bookshop.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class GenresController extends CommonAttributeHandler {

    private final GenreService genreService;


    @GetMapping("/genres")
    public String genresPage(Model model) {

        List<Genre> allGenres = genreService.getAllGenres();
        List<Genre> parents = genreService.parentGenres();

        for (Genre genre : allGenres) {
            if (genre.getBooks().isEmpty()) {
                genre.setBooks(genreService.getBooksForParents(genre));
            }
        }

        model.addAttribute("genreslist", parents);
        return "/genres/index";
    }

    @GetMapping("/books/genres/{slug}")
    public String genres(@PathVariable("slug") String slug, Model model) {

        model.addAttribute("genre", genreService.getGenre(slug));
        model.addAttribute("booksByGenre", genreService.getGenre(slug).getBooks().subList(0, Math.min(20, genreService.getGenre(slug).getBooks().size())));

        return "/genres/slug";
    }

    @GetMapping("/books/genre/{slug}")
    @ResponseBody
    public BooksPageDto genrs(@PathVariable("slug") String slug, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {

        List<Book> bookEntities = genreService.getGenre(slug).getBooks();

        return new BooksPageDto(bookEntities.subList(Math.min(offset, bookEntities.size()), Math.min(offset + limit, bookEntities.size())));

    }
}