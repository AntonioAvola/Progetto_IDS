package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.Comune;
import com.unicam.Model.PuntoGeolocalizzato;

import java.util.Locale;

public class RichiestaComuneDTO {

    //private String nomeComune;
    private String descrizione;
    //private long  idResponsabile;
    private double latitudine;
    private double longitudine;


    public RichiestaComuneDTO(){}
    public RichiestaComuneDTO(/*String nome,*/ String descrizione, /*long id,*/ double lat, double lon){
        //this.nomeComune = nome;
        this.descrizione = descrizione;
        //this.idResponsabile = id;
        this.latitudine = lat;
        this.longitudine = lon;
    }

    /*public String getNomeComune() {
        return nomeComune;
    }

    public void setNomeComune(String nomeComune) {
        this.nomeComune = nomeComune;
    }*/

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /*public long getIdResponsabile() {
        return this.idResponsabile;
    }

    public void setIdResponsabile(long id) {
        this.idResponsabile = id;
    }*/

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

    public Comune ToEntityComune(String nomeComune){
        Comune comune = new Comune();
        comune.setNome(nomeComune);
        return comune;
    }

    public PuntoGeolocalizzato ToEntityPunto(String nomeComune) {
        PuntoGeoBuilder builderPunto = new PuntoGeoBuilder();
        builderPunto.BuildTitolo(nomeComune);
        builderPunto.BuildDescrizione(getDescrizione());
        builderPunto.BuildSpecifica(getLatitudine(), getLongitudine());
        return builderPunto.Result();
    }
}
