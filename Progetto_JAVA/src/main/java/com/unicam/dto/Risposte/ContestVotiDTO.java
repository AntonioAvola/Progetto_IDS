package com.unicam.dto.Risposte;

import java.time.LocalDateTime;

public class ContestVotiDTO {

    private String nomeContest;
    private String descrizione;
    private LocalDateTime fine;
    private String vincitore;

    public ContestVotiDTO(String nomeContest, String descrizione,
                              LocalDateTime fine, String vincitore){
        this.nomeContest = nomeContest;
        this.descrizione = descrizione;
        this.fine = fine;
        this.vincitore = vincitore;
    }

    public String getNomeItinerario() {
        return nomeContest;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getFine() {
        return fine;
    }

    public String getVincitore() {
        return vincitore;
    }
}
