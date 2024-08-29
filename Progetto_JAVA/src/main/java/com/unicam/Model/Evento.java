package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table
public class Evento extends Contenuto{

    private Tempo durata;
    private PuntoGeolocalizzato luogo;

    public Evento(){
        super();
    }

    public PuntoGeolocalizzato getLuogo() {
        return luogo;
    }

    public void setLuogo(PuntoGeolocalizzato luogo) {
        this.luogo = luogo;
    }


    public Tempo getDurata() {
        return durata;
    }

    public void setDurata(Tempo durata){
        this.durata = durata;
    }

}
