package com.example.bookshop.security;

import lombok.Data;

@Data
public class ProfileForm {

    private String name;
    private String mail;
    private String phone;
    private String password;
    private String passwordReply;
}
