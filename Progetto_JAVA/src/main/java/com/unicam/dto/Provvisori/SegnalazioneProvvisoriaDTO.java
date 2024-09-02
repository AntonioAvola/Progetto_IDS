package com.unicam.dto.Provvisori;

import com.unicam.Model.Contenuto;

public class SegnalazioneProvvisoriaDTO<T extends Contenuto> {

    private String nomeContenuto;
    private long idCreatore;
    private String tipo;

    public SegnalazioneProvvisoriaDTO(String nomeContenuto,
                                      long idCreatore,
                                      String tipoContenuto){
        this.idCreatore = idCreatore;
        this.nomeContenuto = nomeContenuto;
        this.tipo = tipoContenuto;
    }

    public String getNomeContenuto() {
        return nomeContenuto;
    }

    public long getIdCreatore() {
        return idCreatore;
    }

    public String getTipo() {
        return tipo;
    }
}
