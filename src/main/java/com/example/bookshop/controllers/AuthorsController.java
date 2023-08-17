package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.BooksPageDto;
import com.example.bookshop.model.entity.author.Author;
import com.example.bookshop.services.AuthorService;
import com.example.bookshop.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Api(description = "authors data")
public class AuthorsController extends CommonAttributeHandler {

    private final AuthorService authorService;
    private final BookService bookService;

    @Value("${limitForSliderData}")
    private Integer limitForSliderData;

    @Value("${limitForPages}")
    private Integer limitForPage;

    @Value("${defaultOffset}")
    private Integer defaultOffset;

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorMap", authorService.getAuthorsData());
        return "/authors/index";
    }

    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<Character, Set<Author>> authors(){
        return authorService.getAuthorsData();
    }

    @GetMapping("/books/authors/{slug}")
    public String author(@PathVariable("slug") String slug, Model model) {

        Author author = Optional
                .ofNullable(authorService.getAuthorBySlug(slug))
                .orElse(new Author());

        model.addAttribute("author", author);
        model.addAttribute("authorsbooks", bookService.getPageAuthorsBooks(slug, defaultOffset, limitForSliderData).getContent());
        model.addAttribute("authorsbookscount", author.getBooks().size());

        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorsbooks(@PathVariable("slug") String slug, Model model) {

        model.addAttribute("author", authorService.getAuthorBySlug(slug));
        model.addAttribute("authorsbooks", bookService.getPageAuthorsBooks(slug, defaultOffset, limitForPage).getContent());
        return "/books/author";
    }

    @GetMapping("/books/author/page/{slug}")
    @ResponseBody
    public BooksPageDto getRestBooks(@PathVariable("slug") String slug, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {

        return new BooksPageDto(bookService.getPageAuthorsBooks(slug, offset, limit).getContent());

    }
}
