package com.unicam.Model;

import com.unicam.Model.BuilderContenuto.PuntoLogicoBuilder;
import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class PuntoLogico extends Contenuto{

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
