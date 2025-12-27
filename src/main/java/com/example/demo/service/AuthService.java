package com.example.demo.service;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.UserAccount;

public interface AuthService {

    UserAccount register(RegisterRequestDto request);

    String login(AuthRequestDto request);
}
