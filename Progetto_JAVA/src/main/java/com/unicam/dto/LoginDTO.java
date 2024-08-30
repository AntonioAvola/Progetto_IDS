package com.unicam.dto;

public class LoginDTO {

    private String username;
    private String password;

    public LoginDTO(String username, String password){
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
