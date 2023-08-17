package com.example.bookshop.security;

import com.example.bookshop.security.jwt.JWTRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity

@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String SIGN_IN = "/signin";
    private final JWTRequestFilter filter;
    private final CustomLogoutHandler logoutHandler;
    private final CustomOauth2SuccessHandler customOauth2SuccessHandler;
    private final OnLoginSuccessHandler onLoginSuccessHandler;
    private final BlacklistTokenFilter blacklistTokenFilter;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile", "/books/rateBook/**", "/books/rateBookReview", "/books/bookReview/**").authenticated()
                .antMatchers(SIGN_IN).not().authenticated()
                .antMatchers("/actuator/prometheus", "/api/v1/query").permitAll()
                .and().formLogin()
                .successHandler(onLoginSuccessHandler)
                .loginPage(SIGN_IN)
                .failureUrl(SIGN_IN)
                .and()
                .logout().logoutUrl("/logout")
                .addLogoutHandler(logoutHandler).logoutSuccessUrl(SIGN_IN).deleteCookies("token")
                .and()
                .oauth2Login().successHandler(customOauth2SuccessHandler)
                .and().exceptionHandling().accessDeniedPage("/");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(blacklistTokenFilter, JWTRequestFilter.class);
    }
}