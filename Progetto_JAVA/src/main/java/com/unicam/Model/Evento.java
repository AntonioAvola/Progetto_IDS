package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
public class Evento extends Contenuto{

    private Tempo durata;

    @OneToOne
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
