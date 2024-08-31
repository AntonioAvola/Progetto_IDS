package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.PuntoGeolocalizzato;

import java.util.List;

public class PuntoGeoDTO {

    private String titolo;
    private String descrizione;
    private double latitutine;
    private double longitudine;

    public PuntoGeoDTO(String titolo, String descrizione, double lat, double lon){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.latitutine = lat;
        this.longitudine = lon;
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

    public double getLatitutine() {
        return latitutine;
    }

    public void setLatitutine(double latitutine) {
        this.latitutine = latitutine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public PuntoGeolocalizzato ToEntity() {
        PuntoGeoBuilder builder = new PuntoGeoBuilder();
        builder.BuildTitolo(getTitolo());
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(getLatitutine(), getLongitudine());
        return builder.Result();
    }
}
