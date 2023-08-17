package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.BooksPageDto;
import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PopularBookController extends CommonAttributeHandler {

    private final BookService bookService;

    @Value("${limitForPages}")
    private Integer limitForPage;

    @Value("${defaultOffset}")
    private Integer defaultOffset;

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks(){
        return bookService.getPopularBooks(defaultOffset, limitForPage).getContent();
    }

    @GetMapping("/books/popularpage")
    public String popularPage() {
        return "books/popular";
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPopularBooks(offset, limit).getContent());
    }
}
