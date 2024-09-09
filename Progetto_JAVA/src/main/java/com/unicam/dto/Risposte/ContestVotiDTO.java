package com.unicam.dto.Risposte;

import java.time.LocalDateTime;

public class ContestVotiDTO {

    private String nomeContest;
    private String descrizione;
    private LocalDateTime fine;
    private int votiFavore;
    private int votiContrari;

    public ContestVotiDTO(String nomeContest, String descrizione,
                              LocalDateTime fine, int votiFavore, int votiContrari){
        this.nomeContest = nomeContest;
        this.descrizione = descrizione;
        this.fine = fine;
        this.votiFavore = votiFavore;
        this.votiContrari = votiContrari;
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

    public int getVotiFavore() {
        return votiFavore;
    }

    public int getVotiContrari() {
        return votiContrari;
    }
}
