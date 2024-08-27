package com.unicam.dto;

import com.unicam.Model.Ruolo;

public class UtenteDTO {
    private String username;
    private String nomeComune;
    private Ruolo ruolo;

    public UtenteDTO(String username, String comune, Ruolo ruolo){
        this.username = username;
        this.nomeComune = comune;
        this.ruolo = ruolo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNomeComune() {
        return nomeComune;
    }

    public void setNomeComune(String nomeComune) {
        this.nomeComune = nomeComune;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
