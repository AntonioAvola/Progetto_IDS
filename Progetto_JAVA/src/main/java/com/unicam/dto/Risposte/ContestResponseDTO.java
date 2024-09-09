package com.unicam.dto.Risposte;

import com.unicam.Model.Tempo;

import java.time.LocalDateTime;

public class ContestResponseDTO {

    private String nomeItinerario;
    private String descrizione;
    private LocalDateTime fine;
    private String autore;

    public ContestResponseDTO(String nomeItinerario, String descrizione,
                              LocalDateTime fine, String autore){
        this.nomeItinerario = nomeItinerario;
        this.descrizione = descrizione;
        this.fine = fine;
        this.autore = autore;
    }

    public String getNomeItinerario() {
        return nomeItinerario;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getFine() {
        return fine;
    }

    public String getAutore() {
        return autore;
    }
}
