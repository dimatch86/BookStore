package com.example.bookshop.controllers;

import com.example.bookshop.errs.EmptySearchException;
import com.example.bookshop.model.dto.BooksPageDto;
import com.example.bookshop.model.dto.SearchWordDto;
import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.tag.Tag;
import com.example.bookshop.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController extends CommonAttributeHandler {

    private final BookService bookService;

    @Value("${limitForSliderData}")
    private Integer limitForSliderData;

    @Value("${monthToSubtract}")
    private Integer monthToSubtract;

    @Value("${limitForPages}")
    private Integer limitForPage;

    @Value("${defaultOffset}")
    private Integer defaultOffset;



    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks(){
        return bookService.getPageOfRecommendedBooks(defaultOffset, limitForSliderData).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks(){
        LocalDate dateTo = LocalDate.now();
        LocalDate dateFrom = dateTo.minusMonths(monthToSubtract);
        return bookService.getBooksByDates(dateFrom, dateTo, defaultOffset, limitForSliderData).getContent();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks(){
        return bookService.getPopularBooks(defaultOffset, limitForSliderData).getContent();
    }


    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
    }

    @ModelAttribute("tags")
    public List<Tag> tags(){
        return bookService.getTags();
    }

    @GetMapping("/")
    public String mainPage() {

        return "index";
    }

    @GetMapping("/books/tags/{slug}")
    public String tags(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("tags", bookService.getBooksByTag(slug, defaultOffset, limitForPage));
        model.addAttribute("tag", bookService.getTag(slug));

        return "/tags/index";
    }

    @GetMapping("/books/tag/{slug}")
    @ResponseBody
    public BooksPageDto tags(@PathVariable("slug") String slug, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {

        return new BooksPageDto(bookService.getBooksByTag(slug, offset, limit).getContent());

    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults",
                    bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }

    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto){
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
    }
}
