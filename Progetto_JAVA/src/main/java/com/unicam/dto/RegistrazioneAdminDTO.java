package com.unicam.dto;

import com.unicam.Model.AdminPiattaforma;
import com.unicam.Model.Ruolo;
import com.unicam.Model.User;

public class RegistrazioneAdminDTO {

    private String name;
    private String username;
    private String email;
    private String password;

    public RegistrazioneAdminDTO(String nome,
                                 String username,
                                 String email,
                                 String password){
        this.name = nome;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminPiattaforma ToEntity(){
        AdminPiattaforma utente = new AdminPiattaforma(getName(),
                getUsername() , getEmail(), getPassword());
        utente.setRuolo(Ruolo.ADMIN);
        return utente;
    }
}
