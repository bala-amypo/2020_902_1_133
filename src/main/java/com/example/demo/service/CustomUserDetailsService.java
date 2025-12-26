package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        if (!email.equals("test@example.com")) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(email)
                .password("{noop}password")
                .roles("USER")
                .build();
    }
}
