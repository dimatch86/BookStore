package com.example.bookshop.errs;

public class BlacklistException extends RuntimeException {

    public BlacklistException(String cause) {
        super(cause);
    }
}