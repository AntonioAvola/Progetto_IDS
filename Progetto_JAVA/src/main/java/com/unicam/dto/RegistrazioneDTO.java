package com.unicam.dto;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;

import java.util.Locale;

public class RegistrazioneDTO {

    private String name;
    private String username;
    private String comune;
    private String email;
    private String password;
    private boolean curatore;
    private boolean animatore;
    private boolean rappresentanteComune;

    public RegistrazioneDTO(String name,
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

    public boolean isCuratore() {
        return curatore;
    }

    public boolean isAnimatore() {
        return animatore;
    }

    public boolean isRappresentanteComune() {
        return rappresentanteComune;
    }

    public User ToEntity(){
        User utente = new User(getName(), getEmail(),
                getPassword(), getComune(), getUsername());
        if (isCuratore())
            utente.setRuoloComune(Ruolo.CURATORE);
        else if (isAnimatore())
            utente.setRuoloComune(Ruolo.ANIMATORE);
        else if (isRappresentanteComune())
            utente.setRuoloComune(Ruolo.COMUNE);
        else
            utente.setRuoloComune(Ruolo.CONTRIBUTOR);
        utente.setComuneVisitato(getComune());
        return utente;
    }
}
