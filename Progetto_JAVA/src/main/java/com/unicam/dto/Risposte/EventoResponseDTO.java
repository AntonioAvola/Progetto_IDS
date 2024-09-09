package com.unicam.dto.Risposte;

import com.unicam.Model.Tempo;

import java.time.LocalDateTime;
import java.util.List;

public class EventoResponseDTO {

    private String nomeItinerario;
    private String descrizione;
    private LuogoDTO luogo;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String autore;

    public EventoResponseDTO(String nomeItinerario, String descrizione,
                             LocalDateTime inizio, LocalDateTime fine,
                             String autore){
        this.nomeItinerario = nomeItinerario;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
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


    public String getAutore() {
        return autore;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public LocalDateTime getFine() {
        return fine;
    }
}
