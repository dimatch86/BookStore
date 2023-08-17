package com.example.bookshop.security;

import com.example.bookshop.errs.PasswordsNotEqualsException;
import com.example.bookshop.errs.UserAlreadyExistException;
import com.example.bookshop.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookstoreUserRegister {

    private final BookstoreUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Transactional
    public void registerNewUser(RegistrationForm registrationForm) throws UserAlreadyExistException {

        if (userRepository.existsByEmail(registrationForm.getEmail())) {
            throw new UserAlreadyExistException(registrationForm.getEmail());
        }
        BookstoreUser user = new BookstoreUser();
        user.setName(registrationForm.getName());
        user.setEmail(registrationForm.getEmail());
        user.setPhone(registrationForm.getPhone());
        user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
        userRepository.save(user);
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {

        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Неверный пароль или имя пользователя");
        }
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    @Transactional
    public void modifyUserProfile(ProfileForm profileForm) {

        if (!profileForm.getPassword().equals(profileForm.getPasswordReply())) {
            throw new PasswordsNotEqualsException("Пароли должны совпадать");
        }

        BookstoreUser bookstoreUser =
                Optional.ofNullable(userRepository.findBookstoreUserByEmail(profileForm.getMail()))
                        .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        bookstoreUser.setPhone(profileForm.getPhone());
        bookstoreUser.setPassword(passwordEncoder.encode(profileForm.getPassword()));

        userRepository.save(bookstoreUser);
    }
}