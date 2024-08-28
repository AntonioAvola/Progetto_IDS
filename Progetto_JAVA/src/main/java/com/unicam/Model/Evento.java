package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

import java.time.LocalDateTime;

public class Evento extends Contenuto{

    private Tempo durata;
    private PuntoMappaSemplice luogo;

    public Evento(){
        super();
    }
    public Evento(Long id, String titolo, String descrizione, UtenteDTO autore, Tempo tempo, PuntoMappaSemplice punto) {
        super(titolo, descrizione, autore);
        this.durata = tempo;
        this.luogo = punto;
    }


    public PuntoMappaSemplice getLuogo() {
        return luogo;
    }

    public void setLuogo(PuntoMappaSemplice luogo) {
        this.luogo = luogo;
    }


    public Tempo getDurata() {
        return durata;
    }

    public void setDurata(Tempo durata){
        this.durata = durata;
    }

}
