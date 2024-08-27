package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

public class PuntoGeolocalizzato extends Contenuto{

    private Double longitudine;
    private Double latitudine;

    public PuntoGeolocalizzato(Long id, String titolo, String descrizione, UtenteDTO autore, Double longitudine, Double latitudine){
        super(titolo, descrizione, autore);
        this.longitudine = longitudine;
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }
}
