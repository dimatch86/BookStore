package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.BooksPageDto;
import com.example.bookshop.model.dto.DateFromDto;
import com.example.bookshop.model.dto.DateToDto;
import com.example.bookshop.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class RecentBookController extends CommonAttributeHandler {

    private final BookService bookService;

    @Value("${monthToSubtract}")
    private Integer monthToSubtract;

    @Value("${limitForPages}")
    private Integer limitForPage;

    @Value("${defaultOffset}")
    private Integer defaultOffset;

    @GetMapping("/books/recentpage")
    public String recent(Model model) {

        LocalDate defaultDateTo = LocalDate.now();
        LocalDate defaultDateFrom = defaultDateTo.minusMonths(monthToSubtract);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        model.addAttribute("dateFromDto", new DateFromDto(formatter.format(defaultDateFrom)));
        model.addAttribute("dateToDto", new DateToDto(formatter.format(defaultDateTo)));
        model.addAttribute("recentBooks", bookService.getBooksByDates(defaultDateFrom, defaultDateTo, defaultOffset, limitForPage));

        return "books/recent";
    }


    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto gePageOfRecentBooks(@RequestParam(value = "from", required = false) DateFromDto from,
                                            @RequestParam(value = "to", required = false) DateToDto to,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit){

        if ((from == null) && (to == null)) {
            LocalDate dateT = LocalDate.now();
            LocalDate dateF = dateT.minusMonths(monthToSubtract);
            return new BooksPageDto(bookService.getBooksByDates(dateF, dateT, offset, limit).getContent());
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateFrom = LocalDate.parse(from.getFrom(), dateTimeFormatter);
        LocalDate dateTo = LocalDate.parse(to.getTo(), dateTimeFormatter);

        return new BooksPageDto(bookService.getBooksByDates(dateFrom, dateTo, offset, limit).getContent());
    }
}
