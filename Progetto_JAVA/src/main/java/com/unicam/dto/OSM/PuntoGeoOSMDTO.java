package com.unicam.dto.OSM;

import java.util.Locale;

public class PuntoGeoOSMDTO {

    private String titolo;
    private String descrizione;

    public PuntoGeoOSMDTO(String titolo, String descrizione){
        this.titolo = titolo.toUpperCase(Locale.ROOT);
        this.descrizione = descrizione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
