package com.example.bookshop.controllers;

import com.example.bookshop.errs.EmptySearchException;
import com.example.bookshop.errs.PasswordsNotEqualsException;
import com.example.bookshop.errs.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("loginError", e);
        return "redirect:/signin";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("userNotFoundError", e);
        return "redirect:/signin";
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExistException(UserAlreadyExistException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("registerError", e);
        return "redirect:/signup";
    }


    @ExceptionHandler(PasswordsNotEqualsException.class)
    public String handlePasswordsNotEqualsException(PasswordsNotEqualsException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("passwordError", e);
        return "redirect:/profile";
    }
}