package com.example.bookshop.errs;

public class EmptySearchException extends Exception{

    public EmptySearchException(String message) {
        super(message);
    }
}
