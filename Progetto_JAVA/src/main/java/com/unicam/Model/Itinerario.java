package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "itinerario")
public class Itinerario extends Contenuto{

    @ManyToMany
    @JoinTable(
            name = "itinerario_punto",
            joinColumns = @JoinColumn(name = "itinerario_id"),
            inverseJoinColumns = @JoinColumn(name = "punto_id")
    )
    //@JoinColumn(name = "itinerario_id", nullable = true)
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
