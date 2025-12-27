package com.example.demo.dto;

public class AuthResponseDto {
    private String token;
    private Long expiresAt;
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }
}