package com.example.bookshop.errs;

public class UserAlreadyExistException extends Exception{

    public UserAlreadyExistException(String email) {
        super(String.format("Пользователь с email: %s уже существует", email));
    }
}
