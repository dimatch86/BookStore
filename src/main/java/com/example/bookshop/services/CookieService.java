package com.example.bookshop.services;

import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final BookRepository bookRepository;

    public void setCookie(String slug, String cookiesName, String cookieContent, HttpServletResponse response) {
        if (cookieContent == null || cookieContent.equals("")) {
            Cookie cookie = new Cookie(cookiesName, slug);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else if (!cookieContent.contains(slug))  {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cookieContent).add(slug);
            Cookie cookie = new Cookie(cookiesName, stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    public List<Book> mapCookieToBooks(String cookieContents) {
        List<Book> books = new ArrayList<>();
        if (!(cookieContents == null || cookieContents.equals(""))) {
            cookieContents = cookieContents.startsWith("/") ? cookieContents.substring(1) : cookieContents;
            cookieContents = cookieContents.endsWith("/") ? cookieContents.substring(0, cookieContents.length() - 1) : cookieContents;
            String[] cookieSlugs = cookieContents.split("/");
            books = bookRepository.findBookEntitiesBySlugIn(cookieSlugs);
        }
        return books;
    }

    @SuppressWarnings("null")
    public void removeCookie(String slug, String cookiesName, String cookieContents, HttpServletResponse response) {

        if (cookieContents != null || !cookieContents.equals("")) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie(cookiesName, String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    public void moveCookie(String slug, String cookiesNameFrom,
                           String cookiesNameTo, String cookieContentsFrom,
                           String cookieContentsTo,HttpServletResponse response) {
        removeCookie(slug, cookiesNameFrom, cookieContentsFrom, response);
        setCookie(slug, cookiesNameTo, cookieContentsTo, response);
    }
}