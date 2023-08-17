package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterServicesController extends CommonAttributeHandler {

    @GetMapping("/faq")
    public String faqPage() {
        return "/faq";
    }

    @GetMapping("/documents")
    public String documents() {
        return "/documents/index";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "/contacts";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "/about";
    }
}
