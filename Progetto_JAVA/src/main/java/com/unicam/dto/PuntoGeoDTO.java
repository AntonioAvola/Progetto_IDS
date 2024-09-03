package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.User;

import java.util.Locale;

public class PuntoGeoDTO {

    private String titolo;
    private String descrizione;
    private double latitudine;
    private double longitudine;

    public PuntoGeoDTO(String titolo, String descrizione, double lat, double lon){
        this.titolo = titolo.toUpperCase(Locale.ROOT);
        this.descrizione = descrizione;
        this.latitudine = lat;
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

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public PuntoGeolocalizzato ToEntity(User user) {
        PuntoGeoBuilder builder = new PuntoGeoBuilder();
        builder.BuildAutore(user);
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(getLatitudine(), getLongitudine());
        return builder.Result();
    }
}
