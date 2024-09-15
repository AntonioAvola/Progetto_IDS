package com.unicam.dto;

public class EsitoContestDTO {

    private String comune;
    private String contest;
    private String descrizione;
    private String esito;

    public EsitoContestDTO(String comune,
                           String contest,
                           String descrizione){
        this.comune = comune;
        this.contest = contest;
        this.descrizione = descrizione;
    }

    public String getComune() {
        return comune;
    }

    public String getContest() {
        return contest;
    }

    public String getEsito() {
        return esito;
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
