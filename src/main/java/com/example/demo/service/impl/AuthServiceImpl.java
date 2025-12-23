package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    // Simple in-memory user store (for demo only)
    private final Map<String, String> users = new HashMap<>();

    @Override
    public void register(RegisterRequestDto request) {
        if (users.containsKey(request.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }
        users.put(request.getUsername(), request.getPassword());
    }

    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        String storedPassword = users.get(request.getUsername());

        if (storedPassword == null || !storedPassword.equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return new AuthResponseDto("login-success");
    }
}
