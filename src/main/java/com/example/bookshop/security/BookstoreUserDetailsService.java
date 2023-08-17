package com.example.bookshop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        BookstoreUser bookstoreUser =
                Optional.ofNullable(bookstoreUserRepository.findBookstoreUserByEmail(s))
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new BookstoreUserDetails(bookstoreUser);
    }
}
