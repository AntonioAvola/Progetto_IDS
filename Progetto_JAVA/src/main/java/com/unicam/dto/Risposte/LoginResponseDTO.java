package com.unicam.dto.Risposte;

import com.unicam.Model.Ruolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginResponseDTO {
    private String token;
    private String username;
    private Ruolo role;
    private String comune;
    private String comuneVisitato;
    private String message;

    //aggiunta attributo Map<String, List<?>> per i contenuti inerenti a un comune
    private Map<String, List<?>> contenutiComune = new HashMap<>();

    // Costruttore vuoto per la deserializzazione
    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, String username, Ruolo role,
                            String comune, String comuneVisitato) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.comune = comune;
        this.comuneVisitato = comuneVisitato;
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

    public Map<String, List<?>> getContenutiComune() {
        return contenutiComune;
    }

    public void setContenutiComune(Map<String, List<?>> contenutiComune) {
        this.contenutiComune = contenutiComune;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getComuneVisitato() {
        return comuneVisitato;
    }

    public void setComuneVisitato(String comuneVisitato) {
        this.comuneVisitato = comuneVisitato;
    }
}
