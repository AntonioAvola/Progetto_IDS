package com.unicam.Model;

import java.util.List;
import java.util.ArrayList;


public class Itinerario extends Contenuto{

    private List<PuntoGeolocalizzato> puntiDiInteresse = new ArrayList<PuntoGeolocalizzato>();

    public Itinerario(Long id, String titolo, String descrizione, TipoContenuto tipo, User autore, PuntoGeolocalizzato puntoDiInteresse, StatoContenuto stato) {
        super(id, titolo, descrizione, tipo, autore, puntoDiInteresse, stato);
    }

    public List<PuntoGeolocalizzato> getPuntiDiInteresse() {
        return puntiDiInteresse;
    }

    public void setPuntiDiInteresse(List<PuntoGeolocalizzato> puntiDiInteresse) {
        this.puntiDiInteresse = puntiDiInteresse;
    }
}
