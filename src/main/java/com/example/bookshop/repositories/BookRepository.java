package com.example.bookshop.repositories;

import com.example.bookshop.model.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBookEntitiesByTitleContaining(String bookTitle);

    List<Book> findBookEntitiesByPriceBetween(Integer min, Integer max);

    List<Book> findBookEntitiesByPrice(Integer price);

    @Query(value = "select * from book where is_bestseller=1", nativeQuery = true)
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM book where discount = (SELECT MAX(discount) from book)", nativeQuery = true)
    List<Book> getBookEntitiesWithMaxDiscount();

    Page<Book> findBookEntityByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);

    @Query(value = "select * from book where pub_date between :dateFrom and :dateTo order by pub_date desc", nativeQuery = true)
    Page<Book> getBooksByPubDateBetween(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo, Pageable pageable);

    @Query(value = "select * from book order by (bought + in_cart * 0.7 + postponed * 0.4) desc", nativeQuery = true)
    Page<Book> getBooksSortedByPopular(Pageable pageable);


    @Query(value = "select * from book b " +
            "join book2tag bt on b.id = bt.book_id " +
            "join tag t on bt.tag_id = t.id where t.slug = :slug", nativeQuery = true)
    Page<Book> getBooksByTag(@Param("slug") String slug, Pageable pageable);

    @Query(value = "select * from book b " +
            "join book2author bauth on b.id = bauth.book_id " +
            "join author a on bauth.author_id = a.id where a.slug = :slug", nativeQuery = true)
    Page<Book> getBooksByAuthor(@Param("slug") String slug, Pageable pageable);

    Book findBookEntityBySlug(String slug);

    List<Book> findBookEntitiesBySlugIn(String[] slugs);


    @Query(value = "select * from book b " +
            "where b.id in (select bu.book_id from book2user bu join book2user_type but" +
            " on bu.type_id = but.id where bu.user_id = :userId and but.code = :code)", nativeQuery = true)
    List<Book> getBooksByUserAndStatus(@Param("userId") long userId, @Param("code") String code);
}
