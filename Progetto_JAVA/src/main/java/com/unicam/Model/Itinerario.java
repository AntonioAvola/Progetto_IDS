package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

import java.util.List;
import java.util.ArrayList;


public class Itinerario extends Contenuto{

    private List<PuntoGeolocalizzato> puntiDiInteresse = new ArrayList<PuntoGeolocalizzato>();

    public Itinerario(Long id, String titolo, String descrizione, UtenteDTO autore) {
        super(titolo, descrizione, autore);
    }

    public List<PuntoGeolocalizzato> getPuntiDiInteresse() {
        return puntiDiInteresse;
    }

    public void setPuntiDiInteresse(List<PuntoGeolocalizzato> puntiDiInteresse) {
        this.puntiDiInteresse = puntiDiInteresse;
    }
}
