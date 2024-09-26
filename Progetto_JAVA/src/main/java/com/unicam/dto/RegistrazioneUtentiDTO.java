package com.unicam.dto;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;

import java.util.Locale;

public class RegistrazioneUtentiDTO {

    private String name;
    private String username;
    private String comune;
    private String email;
    private String password;
    private boolean curatore;
    private boolean animatore;
    private boolean rappresentanteComune;

    public RegistrazioneUtentiDTO(String name,
                                  String username,
                                  String comune,
                                  String email,
                                  String password,
                                  boolean curatore,
                                  boolean animatore,
                                  boolean rappresentante){
        this.name = name;
        this.username = username;
        this.comune = comune.toUpperCase(Locale.ROOT);
        this.email = email;
        this.password = password;
        this.curatore = curatore;
        this.animatore = animatore;
        this.rappresentanteComune = rappresentante;
    }

    public RegistrazioneUtentiDTO(){}

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

    public String getComune() {
        return comune;
    }
    public void setComune(String comune) {
        this.comune = comune;
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

    public boolean isCuratore() {
        return curatore;
    }
    public void setCuratore(boolean curatore) {
        this.curatore = curatore;
    }

    public boolean isAnimatore() {
        return animatore;
    }
    public void setAnimatore(boolean animatore) {
        this.animatore = animatore;
    }

    public boolean isRappresentanteComune() {
        return rappresentanteComune;
    }
    public void setRappresentanteComune(boolean rappresentanteComune) {
        this.rappresentanteComune = rappresentanteComune;
    }

    public User ToEntity(){
        User utente = new User(getName(), getEmail(),
                getPassword(), getComune().toUpperCase(Locale.ROOT), getUsername());
        if (isCuratore())
            utente.setRuoloComune(Ruolo.CURATORE);
        else if (isAnimatore())
            utente.setRuoloComune(Ruolo.ANIMATORE);
        else if (isRappresentanteComune())
            utente.setRuoloComune(Ruolo.COMUNE);
        else
            utente.setRuoloComune(Ruolo.CONTRIBUTOR);
        utente.setComuneVisitato(getComune().toUpperCase(Locale.ROOT));
        return utente;
    }
}
