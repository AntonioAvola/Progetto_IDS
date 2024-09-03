package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

public class PuntoGeoBuilder implements Builder{
    private PuntoGeolocalizzato punto;

    public PuntoGeoBuilder(){
        this.punto = new PuntoGeolocalizzato();
    }

    @Override
    public void BuildAutore(User autore) {
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

    public void BuildSpecifica(double lat, double lon){
        BuildLatitudine(lat);
        BuildLongitudine(lon);
    }

    private void BuildLongitudine(double lon) {
        this.punto.setLongitudine(lon);
    }

    private void BuildLatitudine(double lat) {
        this.punto.setLatitudine(lat);
    }

    public PuntoGeolocalizzato Result(){
        return this.punto;
    }
}
