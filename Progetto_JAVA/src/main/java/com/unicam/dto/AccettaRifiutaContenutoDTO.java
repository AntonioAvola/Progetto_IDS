package com.unicam.dto;

import com.unicam.Model.StatoContenuto;

import java.util.Locale;

public class AccettaRifiutaContenutoDTO {

    private String tipoContenuto;
    private String nomeContenuto;
    private StatoContenuto stato;

    public AccettaRifiutaContenutoDTO(String tipoContenuto,
                                      String nomeContenuto,
                                      StatoContenuto stato){
        this.tipoContenuto = tipoContenuto.toLowerCase(Locale.ROOT);
        this.nomeContenuto = nomeContenuto.toUpperCase(Locale.ROOT);
        this.stato = stato;
    }

    public String getTipoContenuto() {
        return tipoContenuto;
    }

    public void setTipoContenuto(String tipoContenuto) {
        this.tipoContenuto = tipoContenuto;
    }

    public String getNomeContenuto() {
        return nomeContenuto;
    }

    public void setNomeContenuto(String nomeContenuto) {
        this.nomeContenuto = nomeContenuto;
    }

    public StatoContenuto getStato() {
        return stato;
    }

    public void setStato(StatoContenuto stato) {
        this.stato = stato;
    }
}
