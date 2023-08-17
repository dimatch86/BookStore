package com.example.bookshop.controllers;


import com.example.bookshop.model.dto.RateDto;
import com.example.bookshop.model.dto.RateReviewDto;
import com.example.bookshop.model.dto.ReviewDto;
import com.example.bookshop.services.ResourceStorage;
import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.book.BookRate;
import com.example.bookshop.repositories.BookRepository;
import com.example.bookshop.services.RatingService;
import com.example.bookshop.services.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController extends CommonAttributeHandler {

    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final RatingService ratingService;
    private final ReviewService reviewService;


    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {

        Book book = Optional
                .ofNullable(bookRepository.findBookEntityBySlug(slug))
                .orElse(new Book());
        List<BookRate> bookRates = book.getBookRates();
        int myRate = ratingService.getMyRate(slug);

        model.addAttribute("slugBook", book)
        .addAttribute("rate_count", bookRates.size())
        .addAttribute("rate_map", ratingService.getDistributionRatesMap(bookRates))
        .addAttribute("rate_array", ratingService.mapAverageBookRatingToStarsArray(bookRates))
        .addAttribute("my_rate", ratingService.mapRateToStarsArray(myRate))
        .addAttribute("book_reviews", reviewService.getSortedReviewList(book));

        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file")MultipartFile file, @PathVariable("slug") String slug) throws IOException {

        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookRepository.findBookEntityBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        log.info("book file path: {}", path);
        MediaType mediaType = storage.getBookFileMime(hash);
        log.info(" book file mime type: {}", mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        log.info("book file data len: {}", data.length);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/rateBook/{slug}")
    @ResponseBody
    public ResponseEntity<String> rateBook(@RequestBody RateDto rateDto, @PathVariable("slug") String slug,
                                           Model model) {

        ratingService.rateBook(slug, rateDto.getValue());
        model.addAttribute("bookRate", rateDto.getValue());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/bookReview/{slug}")
    @ResponseBody
    public String reviewBook(@PathVariable("slug") String slug, @RequestBody ReviewDto reviewDto) {

        reviewService.saveReview(slug, reviewDto.getText());
        return "redirect:/books/" + slug;
    }

    @PostMapping("/rateBookReview")
    @ResponseBody
    public ResponseEntity<String> rateBookReview(@RequestBody RateReviewDto reviewDto) {

        reviewService.saveReviewLike(reviewDto.getReviewid(), reviewDto.getValue());
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}