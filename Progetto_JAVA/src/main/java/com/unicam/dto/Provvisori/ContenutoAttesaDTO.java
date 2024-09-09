package com.unicam.dto.Provvisori;

import com.unicam.Model.StatoContenuto;

import java.util.Locale;

public class ContenutoAttesaDTO {

    private String tipoContenuto;
    private String nomeContenuto;

    public ContenutoAttesaDTO(String tipoContenuto,
                                      String nomeContenuto){
        this.tipoContenuto = tipoContenuto.toLowerCase(Locale.ROOT);
        this.nomeContenuto = nomeContenuto.toUpperCase(Locale.ROOT);
    }

    public String getTipoContenuto() {
        return tipoContenuto;
    }

    public String getNomeContenuto() {
        return nomeContenuto;
    }

    public void setNomeContenuto(String nomeContenuto) {
        this.nomeContenuto = nomeContenuto;
    }
}
