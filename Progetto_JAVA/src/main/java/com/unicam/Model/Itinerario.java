package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table
public class Itinerario extends Contenuto{

    private List<PuntoGeolocalizzato> puntiDiInteresse = new ArrayList<PuntoGeolocalizzato>();


    public Itinerario(){
        super();
    }

    public List<PuntoGeolocalizzato> getPuntiDiInteresse() {
        return puntiDiInteresse;
    }

    public void setPuntiDiInteresse(List<PuntoGeolocalizzato> puntiDiInteresse) {
        this.puntiDiInteresse = puntiDiInteresse;
    }

}
