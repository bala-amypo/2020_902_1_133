package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // TEMP user for testing (since no DB yet)
        if (!email.equals("test@example.com")) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(email)
                .password("{noop}password") // no encoding for now
                .roles("USER")
                .build();
    }
}
