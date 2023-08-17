package com.example.bookshop.services;


import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.tag.Tag;
import com.example.bookshop.errs.BookstoreApiWrongParameterException;
import com.example.bookshop.repositories.BookRepository;
import com.example.bookshop.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository;


    public List<Book> getBookByTitle(String title) throws BookstoreApiWrongParameterException {

        if (title.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBookEntitiesByTitleContaining(title);
            if (!data.isEmpty()) {
                return data;
            } else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<Book> getBookByPriceBetween(Integer min, Integer max) {
        return bookRepository.findBookEntitiesByPriceBetween(min, max);
    }

    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBookEntitiesByPrice(price);
    }

    public List<Book> getBooksWithMaxDiscount() {
        return bookRepository.getBookEntitiesWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageAuthorsBooks(String slug, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);

        return bookRepository.getBooksByAuthor(slug, nextPage);
    }

    public Page<Book> getPopularBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksSortedByPopular(nextPage);
    }


    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntityByTitleContainingIgnoreCase(searchWord, nextPage);
    }

    public Page<Book> getBooksByDates(LocalDate from, LocalDate to, Integer offset, Integer limit) {

        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksByPubDateBetween(from, to, nextPage);
    }

    public List<Tag> getTags(){
        return tagRepository.findAll();
    }

    public Tag getTag(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

    public Page<Book> getBooksByTag(String slug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksByTag(slug, nextPage);
    }
}