package com.example.demo.dto;

public class AuthResponseDto {

    private String message;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
