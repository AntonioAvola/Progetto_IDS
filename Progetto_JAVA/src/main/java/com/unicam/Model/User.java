package com.unicam.Model;

public class User {
    private String name;
    private String username;
    private String comune;
    private String email;
    private String password;
    protected Ruolo ruolo;

    public User(String name, String email, String password, String comune, String username) {
        this.name = name;
        this.email = email;
        this.comune = comune;
        this.username = username;
        this.ruolo = Ruolo.TURISTA_AUTENTICATO;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getComune(){
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
