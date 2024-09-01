package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.PuntoGeolocalizzato;

import java.util.Locale;

public class PuntoGeoProvvisorioDTO {

    private long idUtente;
    private String titolo;
    private String descrizione;
    private double latitudine;
    private double longitudine;

    public PuntoGeoProvvisorioDTO(long id, String titolo, String descrizione, double lat, double lon){
        this.idUtente = id;
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

    public long getIdUtente() {
        return idUtente;
    }

    public PuntoGeolocalizzato ToEntity() {
        PuntoGeoBuilder builder = new PuntoGeoBuilder();
        builder.BuildAutore(idUtente);
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(getLatitudine(), getLongitudine());
        return builder.Result();
    }

}
