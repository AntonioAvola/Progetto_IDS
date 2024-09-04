package com.unicam.dto.Risposte;

import com.unicam.Model.Tempo;

import java.util.List;

public class EventoResponseDTO {

    private String nomeItinerario;
    private String descrizione;
    private LuogoDTO luogo;
    private Tempo durata;
    private String autore;

    public EventoResponseDTO(String nomeItinerario, String descrizione,
                             Tempo durata, String autore){
        this.nomeItinerario = nomeItinerario;
        this.descrizione = descrizione;
        this.durata = durata;
        this.autore = autore;
    }

    public String getNomeItinerario() {
        return nomeItinerario;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LuogoDTO getLuogo() {
        return luogo;
    }

    public void setLuogo(LuogoDTO luogo){
        this.luogo = luogo;
    }

    public Tempo getDurata() {
        return durata;
    }

    public String getAutore() {
        return autore;
    }
}
