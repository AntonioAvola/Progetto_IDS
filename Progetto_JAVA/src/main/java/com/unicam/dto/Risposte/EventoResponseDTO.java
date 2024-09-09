package com.unicam.dto.Risposte;

import java.time.LocalDateTime;

public class EventoResponseDTO {

    private String nomeEvento;
    private String descrizione;
    private LuogoDTO luogo;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String autore;

    public EventoResponseDTO(String nomeEvento, String descrizione,
                             LocalDateTime inizio, LocalDateTime fine,
                             String autore){
        this.nomeEvento = nomeEvento;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
        this.autore = autore;
    }

    public String getNomeEvento() {
        return nomeEvento;
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
