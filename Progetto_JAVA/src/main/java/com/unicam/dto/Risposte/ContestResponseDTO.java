package com.unicam.dto.Risposte;

import java.time.LocalDateTime;

public class ContestResponseDTO {

    private String nomeContest;
    private String descrizione;
    private LocalDateTime fine;
    private String autore;

    public ContestResponseDTO(String nomeItinerario, String descrizione,
                              LocalDateTime fine, String autore){
        this.nomeContest = nomeItinerario;
        this.descrizione = descrizione;
        this.fine = fine;
        this.autore = autore;
    }

    public String getNomeContest() {
        return nomeContest;
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
