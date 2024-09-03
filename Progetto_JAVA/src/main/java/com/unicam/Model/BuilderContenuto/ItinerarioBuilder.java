package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.Itinerario;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

import java.util.List;

public class ItinerarioBuilder implements Builder{
    private Itinerario itinerario;
    public ItinerarioBuilder(){
        this.itinerario = new Itinerario();
    }


    @Override
    public void BuildAutore(User autore) {
        this.itinerario.setAutore(autore);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.itinerario.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.itinerario.setDescrizione(descrizione);
    }

    public void BuildSpecifica(List<PuntoGeolocalizzato> interessi) {
        this.itinerario.setPuntiDiInteresse(interessi);
    }

    public Itinerario Result(){
        return this.itinerario;
    }
}
