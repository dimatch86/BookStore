package com.example.bookshop.errs;

public class PasswordsNotEqualsException extends RuntimeException{

    public PasswordsNotEqualsException(String message) {
        super(message);
    }
}
