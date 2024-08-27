package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

import java.time.LocalDateTime;

public class Evento extends Contenuto{

    private Tempo durata;
    private PuntoGeolocalizzato luogo;

    public Evento(Long id, String titolo, String descrizione, UtenteDTO autore, Tempo tempo, PuntoGeolocalizzato punto) {
        super(titolo, descrizione, autore);
        this.durata = tempo;
        this.luogo = punto;
    }


    public PuntoGeolocalizzato getLuogo() {
        return luogo;
    }

    public void setLuogo(PuntoGeolocalizzato luogo) {
        this.luogo = luogo;
    }


    public Tempo getDurata() {
        return durata;
    }

}
