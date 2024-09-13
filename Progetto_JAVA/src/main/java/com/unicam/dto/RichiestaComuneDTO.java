package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.Comune;
import com.unicam.Model.PuntoGeolocalizzato;

import java.util.Locale;

public class RichiestaComuneDTO {

    private double latitudine;
    private double longitudine;


    public RichiestaComuneDTO(){}
    public RichiestaComuneDTO(double lat, double lon){
        this.latitudine = lat;
        this.longitudine = lon;
    }


    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public Comune ToEntityComune(String nomeComune){
        Comune comune = new Comune();
        comune.setNome(nomeComune);
        return comune;
    }

    public PuntoGeolocalizzato ToEntityPunto(String nomeComune) {
        PuntoGeoBuilder builderPunto = new PuntoGeoBuilder();
        builderPunto.BuildTitolo("COMUNE");
        builderPunto.BuildDescrizione("comune");
        builderPunto.BuildSpecifica(getLatitudine(), getLongitudine());
        builderPunto.BuildComune(nomeComune);
        return builderPunto.Result();
    }
}
