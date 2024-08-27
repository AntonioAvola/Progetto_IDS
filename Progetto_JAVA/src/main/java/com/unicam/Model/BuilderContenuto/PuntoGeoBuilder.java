package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.PuntoLogico;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

public class PuntoGeoBuilder implements Builder{
    private PuntoGeolocalizzato punto;

    public PuntoGeoBuilder(){
        this.punto = new PuntoGeolocalizzato();
    }

    @Override
    public void BuildAutore(UtenteDTO autore) {
        this.punto.setAutore(autore);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.punto.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.punto.setDescrizione(descrizione);
    }

    public void BuildSpecifica(Double lat, Double lon){
        BuildLatitudine(lat);
        BuildLongitudine(lon);
    }

    private void BuildLongitudine(Double lon) {
        this.punto.setLongitudine(lon);
    }

    private void BuildLatitudine(Double lat) {
        this.punto.setLatitudine(lat);
    }

    public PuntoGeolocalizzato Result(){
        return this.punto;
    }
}
