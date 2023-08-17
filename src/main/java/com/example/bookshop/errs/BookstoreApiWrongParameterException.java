package com.example.bookshop.errs;

public class BookstoreApiWrongParameterException extends Exception{

    public BookstoreApiWrongParameterException(String message) {
        super(message);
    }
}
