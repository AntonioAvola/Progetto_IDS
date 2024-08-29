package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class PuntoGeolocalizzato extends Contenuto{

    private double longitudine;
    private double latitudine;

    public PuntoGeolocalizzato(){
        super();
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }
}
