package com.unicam.Model;

import java.util.List;
import java.util.ArrayList;


public class Itinerario extends Contenuto{

    private List<PuntoGeolocalizzato> puntiDiInteresse = new ArrayList<PuntoGeolocalizzato>();

    public Itinerario(Long id, String titolo, String descrizione, User autore, StatoContenuto stato) {
        super(id, titolo, descrizione, autore, stato);
    }

    public List<PuntoGeolocalizzato> getPuntiDiInteresse() {
        return puntiDiInteresse;
    }

    public void setPuntiDiInteresse(List<PuntoGeolocalizzato> puntiDiInteresse) {
        this.puntiDiInteresse = puntiDiInteresse;
    }
}
