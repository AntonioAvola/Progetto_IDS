package com.unicam.Model;

import com.unicam.Model.BuilderContenuto.PuntoLogicoBuilder;
import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "puntoLogico")
public class PuntoLogico extends Contenuto{

    @OneToOne
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
