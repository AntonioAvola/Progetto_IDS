package com.unicam.Model;

public class PuntoGeolocalizzato {

    private long longitudine;
    private long latitudine;

    public PuntoGeolocalizzato(long longitudine, long latitudine){
        this.longitudine = longitudine;
        this.latitudine = latitudine;
    }

    public long getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(long longitudine) {
        this.longitudine = longitudine;
    }

    public long getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(long latitudine) {
        this.latitudine = latitudine;
    }
}
