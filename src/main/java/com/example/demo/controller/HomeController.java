package com.example.demo.controller;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final AuthService authService;

    public HomeController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserAccount register(
            @RequestParam String email,
            @RequestParam String password) {

        return authService.register(email, password);
    }

    @GetMapping("/test")
    public String test() {
        return "Home Controller Working";
    }
}
