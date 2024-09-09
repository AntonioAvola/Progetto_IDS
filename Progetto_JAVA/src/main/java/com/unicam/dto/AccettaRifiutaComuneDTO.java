package com.unicam.dto;

import com.unicam.Model.StatoContenuto;

import java.util.Locale;

public class AccettaRifiutaComuneDTO {

    private String nomeComune;

    public AccettaRifiutaComuneDTO(String nomeComune){
        this.nomeComune = nomeComune.toUpperCase(Locale.ROOT);
    }

    public String getNomeComune() {
        return nomeComune;
    }
}
