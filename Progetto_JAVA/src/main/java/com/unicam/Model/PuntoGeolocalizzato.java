package com.unicam.Model;

public class PuntoGeolocalizzato {

    private Double longitudine;
    private Double latitudine;

    public PuntoGeolocalizzato(Double longitudine, Double latitudine){
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
