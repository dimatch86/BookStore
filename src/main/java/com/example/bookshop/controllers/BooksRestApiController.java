package com.example.bookshop.controllers;

import com.example.bookshop.model.ApiResponse;
import com.example.bookshop.errs.BookstoreApiWrongParameterException;
import com.example.bookshop.services.BookService;
import com.example.bookshop.model.entity.book.Book;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/by-title")
    @ApiOperation("operation to get books by title")
    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title") String title) throws BookstoreApiWrongParameterException {
        List<Book> data = bookService.getBookByTitle(title);
        ApiResponse<Book> response = ApiResponse.<Book>builder()
                .debugMessage("successful request")
                .message("data size: " + data.size() + " elements")
                .status(HttpStatus.OK)
                .timeStamp(LocalDateTime.now())
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/by-price")
    @ApiOperation("operation to get books by price")
    public ResponseEntity<List<Book>> priceBooks(@RequestParam("price") Integer price) {
        return ResponseEntity.ok(bookService.getBooksWithPrice(price));
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("operation to get books by range from min price to max price")
    public ResponseEntity<List<Book>> priceRangeBooks(@RequestParam("min") Integer min,
                                                      @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBookByPriceBetween(min, max));
    }

    @GetMapping("/books/with-max-price")
    @ApiOperation("operation to get book with max price")
    public ResponseEntity<List<Book>> maxPriceBooks() {
        return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("operation to get bestsellers")
    public ResponseEntity<List<Book>> bestsellerBooks() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters", exception),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Bad parameter value...", exception),
                HttpStatus.BAD_REQUEST);
    }
}
