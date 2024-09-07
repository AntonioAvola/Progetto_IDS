package com.unicam.Model;

import com.unicam.Model.BuilderContenuto.PuntoLogicoBuilder;
import com.unicam.dto.UtenteDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "puntoLogico")
public class PuntoLogico extends Contenuto{

    @ManyToOne
    @JoinColumn(name = "posizione_id")
    private PuntoGeolocalizzato riferimento;

    public PuntoLogico(){
        super();
    }

    public PuntoGeolocalizzato getRiferimento() {
        return riferimento;
    }
    public void setRiferimento(PuntoGeolocalizzato punto){
        this.riferimento = punto;
    }
}
