package com.unicam.dto;

import com.unicam.Model.StatoContenuto;

public class AccettaRifiutaComuneDTO {

    private String nomeComune;
    private StatoContenuto stato;

    public AccettaRifiutaComuneDTO(String nomeComune,
                                   StatoContenuto stato){
        this.nomeComune = nomeComune.toUpperCase();
        this.stato = stato;
    }

    public String getNomeComune() {
        return nomeComune;
    }

    public void setNomeComune(String nomeComune) {
        this.nomeComune = nomeComune;
    }

    public StatoContenuto getStato() {
        return stato;
    }

    public void setStato(StatoContenuto stato) {
        this.stato = stato;
    }
}
