package com.example.bookshop.services;

import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.book.links.Book2User;
import com.example.bookshop.model.entity.book.links.Book2UserType;
import com.example.bookshop.model.entity.book.links.Book2UserTypeEnum;
import com.example.bookshop.repositories.Book2UserRepository;
import com.example.bookshop.repositories.Book2UserTypeRepository;
import com.example.bookshop.repositories.BookRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BookRepository bookRepository;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;

    public List<Book> getBooksFromCart() {
        BookstoreUser currentUser = CurrentUserUtils.getCurrentUser();
        return bookRepository.getBooksByUserAndStatus(currentUser.getId(), String.valueOf(Book2UserTypeEnum.CART));
    }

    public List<Book> getBooksFromPostponed() {
        BookstoreUser currentUser = CurrentUserUtils.getCurrentUser();
        return bookRepository.getBooksByUserAndStatus(currentUser.getId(), String.valueOf(Book2UserTypeEnum.KEPT));
    }

    public List<Book> getPaidBooks() {
        BookstoreUser currentUser = CurrentUserUtils.getCurrentUser();
        return bookRepository.getBooksByUserAndStatus(currentUser.getId(), String.valueOf(Book2UserTypeEnum.PAID));
    }

    @Transactional
    public void changeBookStatus(String slug, String status) {

        BookstoreUser currentUser = CurrentUserUtils.getCurrentUser();
        Book book = bookRepository.findBookEntityBySlug(slug);

        if (book2UserRepository.findBook2UserByBookIdAndUserId(book.getId(), currentUser.getId()) == null) {
            Book2UserType book2UserType = book2UserTypeRepository.findByCode(status);
            Book2User book2User = Book2User.builder()
                    .bookId(book.getId())
                    .userId(currentUser.getId())
                    .typeId(book2UserType.getId())
                    .time(LocalDateTime.now())
                    .build();

            book2UserRepository.save(book2User);
        }
    }

    @Transactional
    public void removeBook(String slug) {

        Book book = Optional.ofNullable(bookRepository.findBookEntityBySlug(slug)).orElse(new Book());
        book2UserRepository.deleteBook2UserByBookIdAndUserId(book.getId(), CurrentUserUtils.getCurrentUser().getId());
    }

    @Transactional
    public void updateBookStatus(String slug, String code) {

        Book book = Optional.ofNullable(bookRepository.findBookEntityBySlug(slug)).orElse(new Book());
        Book2User book2User = book2UserRepository.findBook2UserByBookIdAndUserId(book.getId(), CurrentUserUtils.getCurrentUser().getId());
        book2User.setTypeId(book2UserTypeRepository.findByCode(code).getId());
        book2UserRepository.save(book2User);
    }
}