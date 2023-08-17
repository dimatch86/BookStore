package com.example.bookshop.security;

import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.book.links.Book2User;
import com.example.bookshop.model.entity.book.links.Book2UserTypeEnum;
import com.example.bookshop.repositories.Book2UserRepository;
import com.example.bookshop.repositories.Book2UserTypeRepository;
import com.example.bookshop.repositories.BookRepository;
import com.example.bookshop.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final BookRepository bookRepository;
    private final JWTUtil jwtUtil;

    @Value("${cookiename.cart}")
    private String cartContents;

    @Value("${cookiename.postponed}")
    private String postponed;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(email);

        if (bookstoreUser == null) {
            bookstoreUser = saveNewUser(name, email);
        }


        String jwtToken = generateJwt(email);

        addJwtIntoCookie(jwtToken, response);

        mergeBooksFromCookie(getContentFromCookie(request, cartContents), bookstoreUser.getId(), Book2UserTypeEnum.CART);
        mergeBooksFromCookie(getContentFromCookie(request, postponed), bookstoreUser.getId(), Book2UserTypeEnum.KEPT);
        response.sendRedirect("/my");

    }

    private BookstoreUser saveNewUser(String name, String email) {
        BookstoreUser bookstoreUser = BookstoreUser.builder()
                .name(name)
                .email(email)
                .password("somepassword")
                .build();
        return bookstoreUserRepository.save(bookstoreUser);
    }

    private String generateJwt(String email) {
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }

    private void addJwtIntoCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void mergeBooksFromCookie(String[] books, long userId, Book2UserTypeEnum book2UserTypeEnum) {

        if (books.length != 0) {

            List<Book> bookList = bookRepository.findBookEntitiesBySlugIn(books);
            List<Book2User> book2Users = new ArrayList<>();

            for (Book book : bookList) {
                if (book2UserRepository.findBook2UserByBookIdAndUserId(book.getId(), userId) == null) {
                    Book2User book2User = new Book2User();
                    int typeId = book2UserTypeRepository.findByCode(String.valueOf(book2UserTypeEnum)).getId();
                    book2User.setUserId(userId);
                    book2User.setBookId(book.getId());
                    book2User.setTypeId(typeId);
                    book2User.setTime(LocalDateTime.now());

                    book2Users.add(book2User);
                }
            }
            book2UserRepository.saveAll(book2Users);
        }
    }

    private String[] getContentFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String content = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)) {
                    content = cookie.getValue();

                }
            }
        }
        return content.split("/");
    }
}