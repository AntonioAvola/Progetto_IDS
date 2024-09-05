package com.unicam.dto;

import java.util.Locale;

public class AggiungiPreferitoDTO {

    private String tipoContenuto;
    private String nomeContenuto;

    public AggiungiPreferitoDTO(String tipoContenuto, String nomeContenuto){
        this.tipoContenuto = tipoContenuto.toLowerCase(Locale.ROOT);
        this.nomeContenuto = nomeContenuto.toUpperCase(Locale.ROOT);
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
}
