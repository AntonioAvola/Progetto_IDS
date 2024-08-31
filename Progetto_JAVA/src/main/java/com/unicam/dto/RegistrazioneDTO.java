package com.unicam.dto;

public class RegistrazioneDTO {

    private String name;
    private String username;
    private String comune;
    private String email;
    private String password;

    public RegistrazioneDTO(String name,
                            String username,
                            String comune,
                            String email,
                            String password){
        this.name = name;
        this.username = username;
        this.comune = comune;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getComune() {
        return comune;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
