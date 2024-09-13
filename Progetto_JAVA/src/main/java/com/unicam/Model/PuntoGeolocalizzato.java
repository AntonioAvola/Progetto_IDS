package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "puntoGeolocalizzato")
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuntoGeolocalizzato that = (PuntoGeolocalizzato) o;
        return Objects.equals(longitudine, that.longitudine) &&
                Objects.equals(latitudine, that.latitudine);
    }


    @Override
    public int hashCode() {
        return Objects.hash(longitudine, latitudine);
    }
}
