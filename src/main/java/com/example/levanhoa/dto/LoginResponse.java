package com.example.levanhoa.dto;

public class LoginResponse {
    private int id;
    private String email;
    private String role;

    public LoginResponse(int id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // Getter, setter
}
