package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.StatusDto;
import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.services.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class ChangeBookStatusController extends CommonAttributeHandler {

    private static final String CART_CONTENT = "cartContents";
    private static final String POSTPONED_CONTENT = "postponed";

    @ModelAttribute(name = "cartBooks")
    public List<Book> booksCart() {
        return new ArrayList<>();
    }
    private final CookieService cookieService;

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        model.addAttribute("cartBooks", cookieService.mapCookieToBooks(cartContents));
        return "/cart";
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(value = "postponed", required = false) String postponed,
                                         Model model) {
        model.addAttribute("postponedBooks", cookieService.mapCookieToBooks(postponed));
        return "/postponed";
    }

    @PostMapping("/changeBookStatus/{slug}")
    @ResponseBody
    public String changeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", defaultValue = "") String cartContents,
                                         @CookieValue(name = "postponed", defaultValue = "") String postponed,
                                         HttpServletResponse response,
                                         @RequestBody StatusDto statusDto) {

        switch (statusDto.getStatus()) {
            case "CART" :
                cookieService.setCookie(slug, CART_CONTENT, cartContents, response);
                break;

            case "KEPT" :
                cookieService.setCookie(slug, POSTPONED_CONTENT, postponed, response);
                break;

            default:
                break;
        }
        return "redirect:/books/" + slug;
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String removeBookFromCart(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", defaultValue = "", required = false) String cartContents,
                                                  HttpServletResponse response) {

        cookieService.removeCookie(slug, CART_CONTENT, cartContents, response);
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String removeBookFromPostponed(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "postponed", defaultValue = "", required = false) String postponed,
                                                  HttpServletResponse response) {

        cookieService.removeCookie(slug, POSTPONED_CONTENT, postponed, response);
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/cart/move/{slug}")
    public String moveBookFromCartToPostponed(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", defaultValue = "", required = false) String cartContents,
                                                  @CookieValue(name = "postponed", defaultValue = "") String postponed,
                                                  HttpServletResponse response) {

        cookieService.moveCookie(slug, CART_CONTENT, POSTPONED_CONTENT, cartContents, postponed, response);
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/postponed/move/{slug}")
    public String moveBookFromPostponedToCart(@PathVariable("slug") String slug,
                                                    @CookieValue(name = "cartContents", defaultValue = "", required = false) String cartContents,
                                                    @CookieValue(name = "postponed", defaultValue = "") String postponed,
                                                    HttpServletResponse response) {

        cookieService.moveCookie(slug, POSTPONED_CONTENT, CART_CONTENT, postponed, cartContents, response);
        return "redirect:/books/postponed";
    }
}