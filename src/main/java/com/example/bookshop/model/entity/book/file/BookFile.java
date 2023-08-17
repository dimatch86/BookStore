package com.example.bookshop.model.entity.book.file;



import com.example.bookshop.model.entity.book.Book;
import com.example.bookshop.model.entity.book.BookFileType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book_file")
@Data
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(name = "type_id", columnDefinition = "INT NOT NULL")
    private int typeId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public String getBookFileExtensionString() {
        return BookFileType.getExtensionStringByTypeId(typeId);
    }
}