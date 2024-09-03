package com.unicam.dto;

import com.unicam.Model.Ruolo;

public class LoginResponseDTO {
    private String token;
    private String username;
    private Ruolo role;
    private String message;

    // Costruttore vuoto per la deserializzazione
    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, String username, Ruolo role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Ruolo getRole() {
        return role;
    }

    public void setRole(Ruolo role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
