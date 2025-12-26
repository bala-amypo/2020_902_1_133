package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserAccountRepository repo,
                           PasswordEncoder encoder) {
        this.userAccountRepository = repo;
        this.passwordEncoder = encoder;
    }

    @Override
    public void register(RegisterRequestDto dto) {

        if (userAccountRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserAccount user = new UserAccount();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");

        userAccountRepository.save(user);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {

        UserAccount user = userAccountRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponseDto("Login successful");
    }
}
