package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.SearchWordDto;
import com.example.bookshop.model.entity.book.links.Book2UserTypeEnum;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
public class CommonAttributeHandler {

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("postponedCount")
    public int countInPostponedUnauthorized(@CookieValue(value = "postponed", required = false) String postponed) {

        return (postponed != null && !postponed.isEmpty()) ?
                postponed.split("/").length : 0;
    }

    @ModelAttribute("cartCount")
    public int countInCartUnauthorized(@CookieValue(value = "cartContents", required = false) String cartContents) {

        return (cartContents != null && !cartContents.isEmpty()) ?
                cartContents.split("/").length : 0;
    }

    @ModelAttribute("authStatus")
    public String status() {

        return CurrentUserUtils.checkUserAuthenticated() ?
                "authorized" : "unauthorized";
    }
    @ModelAttribute("curUser")
    public BookstoreUser user(){

        return CurrentUserUtils.checkUserAuthenticated() ?
                CurrentUserUtils.getCurrentUser() : new BookstoreUser();
    }

    @ModelAttribute("cartCountAuth")
    public int countInCartAuthorized() {

        return cartBlockAmount(Book2UserTypeEnum.CART);
    }

    @ModelAttribute("postponedCountAuth")
    public int countInPostponedAuthorized() {

        return cartBlockAmount(Book2UserTypeEnum.KEPT);
    }

    @ModelAttribute("myBooksCount")
    public int countInMy() {

        return cartBlockAmount(Book2UserTypeEnum.PAID);
    }

    private int cartBlockAmount(Book2UserTypeEnum book2UserTypeEnum) {
        if (CurrentUserUtils.checkUserAuthenticated()) {
            BookstoreUser bookstoreUser = CurrentUserUtils.getCurrentUser();

            return (int) bookstoreUser.getBook2Users()
                    .stream()
                    .filter(book2User -> book2User.getBook2UserType().getCode().equals(String.valueOf(book2UserTypeEnum)))
                    .count();
        }
        return 0;
    }
}