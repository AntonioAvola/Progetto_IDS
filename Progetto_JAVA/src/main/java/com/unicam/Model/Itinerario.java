package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "itinerario")
public class Itinerario extends Contenuto{

    @OneToMany
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
