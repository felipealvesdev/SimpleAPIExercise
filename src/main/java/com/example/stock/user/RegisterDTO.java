package com.example.stock.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
