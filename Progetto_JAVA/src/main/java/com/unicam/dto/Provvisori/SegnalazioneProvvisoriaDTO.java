package com.unicam.dto.Provvisori;

import com.unicam.Model.Contenuto;

import java.util.Locale;

public class SegnalazioneProvvisoriaDTO<T extends Contenuto> {

    private String tipo;
    private String nomeContenuto;

    public SegnalazioneProvvisoriaDTO(String tipoContenuto,
                                      String nomeContenuto){
        this.nomeContenuto = nomeContenuto;
        this.tipo = tipoContenuto;
    }

    public String getNomeContenuto() {
        return nomeContenuto;
    }

    public String getTipo() {
        return tipo;
    }
}
